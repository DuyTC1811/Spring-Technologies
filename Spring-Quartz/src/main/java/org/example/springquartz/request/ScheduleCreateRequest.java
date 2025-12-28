package org.example.springquartz.request;

import org.example.springquartz.enums.ScheduleType;

import java.util.Map;

public record ScheduleCreateRequest(
        String jobName,
        String jobGroup,
        String triggerName,
        String triggerGroup,
        ScheduleType scheduleType,
        String cronExpression,
        Long repeatIntervalMs,
        Integer repeatCount,
        Map<String, Object> jobData
) { }
