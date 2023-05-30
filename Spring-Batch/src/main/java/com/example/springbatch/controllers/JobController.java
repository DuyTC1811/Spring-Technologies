//package com.example.springbatch.controllers;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.JobParametersInvalidException;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
//import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
//import org.springframework.batch.core.repository.JobRestartException;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/jobs")
//public class JobController {
//    private final JobLauncher jobLauncher;
//    private final Job job;
//
//    public JobController(JobLauncher jobLauncher, Job job) {
//        this.jobLauncher = jobLauncher;
//        this.job = job;
//    }
//
//    @PostMapping("/importCustomers")
//    public void importCsvToDBJob() {
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
//
//        try {
//            jobLauncher.run(job, jobParameters);
//        } catch (JobExecutionAlreadyRunningException
//                 | JobRestartException
//                 | JobInstanceAlreadyCompleteException
//                 | JobParametersInvalidException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
