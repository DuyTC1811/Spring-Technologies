package com.example.springbatch.configuration;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Configuration
@EnableScheduling
@EnableBatchProcessing
public class BatchConfigurations {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchConfigurations.class);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    @Scheduled(fixedDelay = 30000)      // mỗi 30s chạy lại sau khi task trước hoàn thành
    @SchedulerLock(
            name = "TaskScheduler_scheduledTask",
            lockAtLeastFor = "30s",     // thời gian giữ lock ít nhất là 30 giây
            lockAtMostFor = "10m"       // nếu job bị lỗi, lock vẫn sẽ tự giải phóng sau 10 phút
    )
void scheduleByFixedRate() throws Exception {
        LOGGER.info("Batch job starting");
        JobParameters jobParameters = new JobParametersBuilder().addString("time", format.format(Calendar.getInstance().getTime())).toJobParameters();
        Thread.sleep(15000);
        LOGGER.info("Job Parameters: {}", jobParameters);
        LOGGER.info("Batch job executed successfully\n");
    }
}
