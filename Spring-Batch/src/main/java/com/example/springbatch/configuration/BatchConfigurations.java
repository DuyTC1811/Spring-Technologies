package com.example.springbatch.configuration;

import com.example.springbatch.job.tasklets.LinesProcessor;
import com.example.springbatch.job.tasklets.LinesReader;
import com.example.springbatch.job.tasklets.LinesWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
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
    private final LinesWriter linesWriter;
    private final LinesReader linesReader;
    private final JobLauncher jobLauncher;
    private final LinesProcessor linesProcessor;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    public BatchConfigurations(
            LinesWriter linesWriter,
            LinesReader linesReader,
            JobLauncher jobLauncher,
            LinesProcessor linesProcessor,
            JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory) {
        this.linesWriter = linesWriter;
        this.linesReader = linesReader;
        this.jobLauncher = jobLauncher;
        this.linesProcessor = linesProcessor;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Step stepLinesReader() {
        return stepBuilderFactory
                .get("LinesReader")
                .tasklet(linesReader)
                .build();
    }

    @Bean
    public Step stepLinesProcessor() {
        return stepBuilderFactory
                .get("Processor")
                .tasklet(linesProcessor)
                .build();
    }

    @Bean
    public Step stepLinesWriter() {
        return stepBuilderFactory
                .get("stepLinesWriter")
                .tasklet(linesWriter)
                .build();
    }

    @Bean
    public Job importOrderJob() {
        return jobBuilderFactory.get("importOrderJob")
                .incrementer(new RunIdIncrementer())
                .flow(stepLinesReader())
                .next(stepLinesProcessor())
                .next(stepLinesWriter())
                .end()
                .build();
    }

    @Scheduled(fixedRate = 5000)
    public void scheduleByFixedRate() throws Exception {
        LOGGER.info("Batch job starting");
        JobParameters jobParameters = new JobParametersBuilder().addString("time", format.format(Calendar.getInstance().getTime())).toJobParameters();
        jobLauncher.run(importOrderJob(), jobParameters);
        LOGGER.info("Batch job executed successfully\n");
    }
}
