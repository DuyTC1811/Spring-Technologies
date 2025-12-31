package org.example.springquartz.model;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.example.springquartz.utils.ConvertUtils.objectToJson;

@Component
public class CronJobInfo implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(CronJobInfo.class);

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        LOGGER.info("[CRON-JOB-INFO] DATA MAP: {}", objectToJson(jobDataMap));
    }
}
