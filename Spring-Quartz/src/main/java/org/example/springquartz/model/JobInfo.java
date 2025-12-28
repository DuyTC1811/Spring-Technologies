package org.example.springquartz.model;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JobInfo implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobInfo.class);
    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Object object = jobDataMap.get(JobInfo.class.getSimpleName());
        LOGGER.info("Remaining fire count is '{}'", object);
    }
}
