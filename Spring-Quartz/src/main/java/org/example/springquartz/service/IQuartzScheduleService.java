package org.example.springquartz.service;

import org.example.springquartz.request.ScheduleCreateRequest;
import org.example.springquartz.request.ScheduleUpsertRequest;

public interface IQuartzScheduleService {
    void cronSchedule(ScheduleCreateRequest req);

    void simpleSchedule(ScheduleCreateRequest req);

    void updateCronSchedule(String triggerName, String triggerGroup, ScheduleUpsertRequest req);

    void pause(String triggerName, String triggerGroup);

    void resume(String triggerName, String triggerGroup);

    void deleteJob(String jobName, String jobGroup);

    void preDestroy();
}
