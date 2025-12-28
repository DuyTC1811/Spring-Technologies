//package org.example.springquartz.util;
//
//import org.example.springquartz.model.TimerInfo;
//import org.quartz.JobBuilder;
//import org.quartz.JobDataMap;
//import org.quartz.JobDetail;
//import org.quartz.SimpleScheduleBuilder;
//import org.quartz.Trigger;
//import org.quartz.TriggerBuilder;
//
//import java.util.Date;
//
//public final class TimerUtils {
//    private TimerUtils() {
//        throw new UnsupportedOperationException("Utility class");
//    }
//
//    public static JobDetail buildJobDetail(final Class jobClass, final TimerInfo info) {
//        final JobDataMap jobDataMap = new JobDataMap();
//        jobDataMap.put(jobClass.getSimpleName(), info);
//
//        return JobBuilder
//                .newJob(jobClass)
//                .withIdentity(jobClass.getSimpleName())
//                .setJobData(jobDataMap)
//                .build();
//    }
//
//    public static Trigger buildTrigger(final Class jobClass, final TimerInfo info) {
//        SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInMilliseconds(info.getRepeatIntervalMs());
//
//        if (info.isRunForever()) {
//            builder = builder.repeatForever();
//        } else {
//            builder = builder.withRepeatCount(info.getTotalFireCount() - 1);
//        }
//
//        return TriggerBuilder
//                .newTrigger()
//                .withIdentity(jobClass.getSimpleName())
//                .withSchedule(builder)
//                .startAt(new Date(System.currentTimeMillis() + info.getInitialOffsetMs()))
//                .build();
//    }
//}
