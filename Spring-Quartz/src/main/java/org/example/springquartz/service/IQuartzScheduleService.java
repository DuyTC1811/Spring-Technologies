package org.example.springquartz.service;

import org.example.springquartz.model.TimerInfo;
import org.example.springquartz.request.ScheduleCreateRequest;
import org.example.springquartz.request.ScheduleUpsertRequest;

import java.util.List;

public interface IQuartzScheduleService {
    void schedule(ScheduleCreateRequest req);

    void updateSchedule(String triggerName, String triggerGroup, ScheduleUpsertRequest req);

    void pause(String triggerName, String triggerGroup);

    void resume(String triggerName, String triggerGroup);

    void deleteJob(String jobName, String jobGroup);


    List<TimerInfo> getAllRunningTimers();

    void preDestroy();
}
