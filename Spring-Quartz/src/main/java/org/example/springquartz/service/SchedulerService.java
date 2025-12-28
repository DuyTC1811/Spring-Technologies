package org.example.springquartz.service;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PreDestroy;
import org.example.springquartz.enums.ScheduleType;
import org.example.springquartz.model.JobInfo;
import org.example.springquartz.request.ScheduleCreateRequest;
import org.example.springquartz.request.ScheduleUpsertRequest;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class SchedulerService implements IQuartzScheduleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerService.class);
    private final Scheduler scheduler;

    public SchedulerService(final Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void schedule(ScheduleCreateRequest req) {
        JobKey jobKey = JobKey.jobKey(req.jobName(), req.jobGroup());
        TriggerKey triggerKey = TriggerKey.triggerKey(req.triggerName(), req.triggerGroup());

        try {
            if (scheduler.checkExists(jobKey)) {
                LOGGER.warn("JOB ALREADY EXISTS: {}", jobKey);
                throw new IllegalStateException("Job already exists: " + jobKey);
            }
            if (scheduler.checkExists(triggerKey)) {
                LOGGER.warn("TRIGGER ALREADY EXISTS: {}", triggerKey);
                throw new IllegalStateException("Trigger already exists: " + triggerKey);
            }

            //  CREATE JOB DETAIL
            JobDetail jobDetail = newJob(JobInfo.class)
                    .withIdentity(jobKey)
                    .usingJobData(toJobDataMap(req.jobData()))
                    .build();

            // CREATE TRIGGER + SCHEDULE
            Trigger trigger = buildTrigger(
                    req.scheduleType(),
                    req.cronExpression(),
                    jobKey,
                    req.repeatIntervalMs(),
                    req.repeatCount(),
                    triggerKey
            );
            scheduler.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            LOGGER.error("[ ERROR ] :: Failed to schedule job: [{}]", e.getMessage());
            throw new RuntimeException("Failed to add schedule", e);
        }
    }

    @Override
    public void updateSchedule(String triggerName, String triggerGroup, ScheduleUpsertRequest req) {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);

        try {
            Trigger oldTrigger = scheduler.getTrigger(triggerKey);
            if (oldTrigger == null) {
                LOGGER.warn("TRIGGER NOT FOUND: {}", triggerKey);
                throw new IllegalStateException("Trigger not found: " + triggerKey);
            }

            // ✅ jobKey thật từ trigger đang tồn tại
            JobKey jobKey = oldTrigger.getJobKey();

            // optional: nếu bạn muốn validate req.jobName/jobGroup phải khớp:
            if (req.jobName() != null && req.jobGroup() != null) {
                JobKey reqJobKey = JobKey.jobKey(req.jobName(), req.jobGroup());
                if (!reqJobKey.equals(jobKey)) {
                    LOGGER.error("Request jobKey does not match existing trigger's jobKey. req={}, existing={}", reqJobKey, jobKey);
                    throw new IllegalArgumentException(
                            "Request jobKey does not match existing trigger's jobKey. req=" + reqJobKey + ", existing=" + jobKey
                    );
                }
            }

            if (!scheduler.checkExists(jobKey)) {
                LOGGER.warn("JOB NOT FOUND for triggerKey={}, jobKey={}", triggerKey, jobKey);
                throw new IllegalStateException("Job not found: " + jobKey);
            }

            // (optional) update jobDataMap nếu gửi lên
            if (req.jobData() != null && !req.jobData().isEmpty()) {
                JobDetail existing = scheduler.getJobDetail(jobKey);
                if (existing != null) {
                    JobDataMap merged = new JobDataMap(existing.getJobDataMap());
                    merged.putAll(req.jobData());

                    JobDetail updatedJob = existing.getJobBuilder()
                            .usingJobData(merged)
                            .build();

                    scheduler.addJob(updatedJob, true); // replace=true
                }
            }

            // ✅ build trigger mới với CÙNG triggerKey
            Trigger newTrigger = buildTrigger(
                    req.scheduleType(),
                    req.cronExpression(),
                    jobKey,
                    req.repeatIntervalMs(),
                    req.repeatCount(),
                    triggerKey
            );

            scheduler.rescheduleJob(triggerKey, newTrigger);

        } catch (SchedulerException e) {
            LOGGER.error("[ERROR] Failed to update schedule. triggerKey={}, msg={}",
                    triggerKey, e.getMessage(), e);
            throw new RuntimeException("Failed to update schedule", e);
        }
    }

    @Override
    public void pause(String triggerName, String triggerGroup) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(triggerName, triggerGroup));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resume(String triggerName, String triggerGroup) {
        try {
            scheduler.resumeTrigger(TriggerKey.triggerKey(triggerName, triggerGroup));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteJob(String jobName, String jobGroup) {
        try {
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreDestroy
    public void preDestroy() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            LOGGER.error("[ ERROR ] :: shutting down scheduler: [{}]", e.getMessage());
        }
    }

    private Trigger buildTrigger(
            ScheduleType type,
            String cronExpression,
            JobKey jobKey,
            Long repeatIntervalMs,
            Integer repeatCount,
            TriggerKey triggerKey) {

        TriggerBuilder<Trigger> tb = newTrigger()
                .withIdentity(triggerKey)
                .forJob(jobKey);

        return switch (type) {
            case CRON -> {
                if (StringUtils.isBlank(cronExpression)) {
                    throw new IllegalArgumentException("cronExpression is required for CRON schedule");
                }
                yield tb.withSchedule(CronScheduleBuilder
                                .cronSchedule(cronExpression))
                        .build();
            }
            case SIMPLE -> {
                if (repeatIntervalMs == null || repeatIntervalMs <= 0) {
                    throw new IllegalArgumentException("repeatIntervalMs must be > 0 for SIMPLE schedule");
                }
                SimpleScheduleBuilder ssb = SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInMilliseconds(repeatIntervalMs);
                if (repeatCount == null) {
                    ssb = ssb.repeatForever();
                } else {
                    ssb = ssb.withRepeatCount(repeatCount);
                }

                yield tb.withSchedule(ssb).build();
            }
        };
    }

    private JobDataMap toJobDataMap(Map<String, Object> jobData) {
        JobDataMap jobDataMap = new JobDataMap();
        if (jobData != null) {
            jobDataMap.putAll(jobData);
        }
        return jobDataMap;
    }
}
