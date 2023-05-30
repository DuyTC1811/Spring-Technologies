//package com.example.springbatch.job.chunks;
//
//import com.example.springbatch.entity.Customer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.core.ExitStatus;
//import org.springframework.batch.core.StepExecution;
//import org.springframework.batch.core.StepExecutionListener;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FileExampleProcessor implements ItemProcessor<Customer, Customer>, StepExecutionListener {
//    private static final Logger LOGGER = LoggerFactory.getLogger(FileExampleProcessor.class);
//
//    @Override
//    public Customer process(Customer customer) {
//        return customer;  // Sử lý logic tại đây
//    }
//
//    @Override
//    public void beforeStep(StepExecution stepExecution) {
//        LOGGER.debug("Lines Processor initialized => {}", stepExecution.getStartTime());
//    }
//
//    @Override
//    public ExitStatus afterStep(StepExecution stepExecution) {
//        LOGGER.debug("Lines Processor ended => {}", stepExecution.getEndTime());
//        return ExitStatus.COMPLETED;
//    }
//}
