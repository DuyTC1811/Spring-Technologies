//package com.example.springbatch.job.chunks;
//
//import com.example.springbatch.entity.Customer;
//import com.example.springbatch.repository.CustomerMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.core.ExitStatus;
//import org.springframework.batch.core.StepExecution;
//import org.springframework.batch.core.StepExecutionListener;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
//@Configuration
//public class FileExampleWriter implements ItemWriter<Customer>, StepExecutionListener {
//    private static final Logger LOGGER = LoggerFactory.getLogger(FileExampleWriter.class);
//    private final CustomerMapper customerMapper;
//
//    public FileExampleWriter(CustomerMapper customerMapper) {
//        this.customerMapper = customerMapper;
//    }
//
//    @Override
//    public void beforeStep(StepExecution stepExecution) {
//        LOGGER.debug("Lines Writer initialized => {}", stepExecution.getStartTime());
//    }
//
//    @Override
//    public ExitStatus afterStep(StepExecution stepExecution) {
//        LOGGER.debug("Lines Writer ended => {}", stepExecution.getEndTime());
//        return ExitStatus.COMPLETED;
//    }
//
//    @Override
//    public void write(List<? extends Customer> items) {
//        items.forEach(customerMapper::insert);
//    }
//}
