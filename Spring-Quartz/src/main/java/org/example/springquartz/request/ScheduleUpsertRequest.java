package org.example.springquartz.request;

import org.example.springquartz.enums.ScheduleType;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

import java.util.Map;

public record ScheduleUpsertRequest(
        String jobName,
        String jobGroup,
        String triggerName,
        String triggerGroup,
        ScheduleType scheduleType,
        String cronExpression,
        Long repeatIntervalMs,
        Integer repeatCount,
        Map<String, Object> jobData
) {
}
