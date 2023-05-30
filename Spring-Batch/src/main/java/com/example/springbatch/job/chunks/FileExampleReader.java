//package com.example.springbatch.job.chunks;
//
//import com.example.springbatch.entity.Customer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.core.ExitStatus;
//import org.springframework.batch.core.StepExecution;
//import org.springframework.batch.core.StepExecutionListener;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.LineMapper;
//import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.FileSystemResource;
//
//@Configuration
//public class FileExampleReader implements ItemReader<FlatFileItemReader<Customer>>, StepExecutionListener {
//    private static final Logger LOGGER = LoggerFactory.getLogger(FileExampleReader.class);
//
//    @Override
//    public FlatFileItemReader<Customer> read() { // Đọc từ file CSV
//        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
//        itemReader.setResource(new FileSystemResource("src/main/resources/static/customers.csv"));
//        itemReader.setName("csvReader");
//        itemReader.setLinesToSkip(1);
//        itemReader.setLineMapper(lineMapper());
//        LOGGER.debug("Lines Reader initialized");
//        return itemReader;
//    }
//
//    private LineMapper<Customer> lineMapper() {                                                                             // map từng dòng
//        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
//
//        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//        lineTokenizer.setDelimiter(",");                                                                                    // chỉ da chỗ phân cách bằng giấu phẩy hoặc các giấu khác dùng để phân cách
//        lineTokenizer.setStrict(true);
//        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");            // những field cần map
//
//        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
//        fieldSetMapper.setTargetType(Customer.class);
//
//        lineMapper.setLineTokenizer(lineTokenizer);
//        lineMapper.setFieldSetMapper(fieldSetMapper);
//        return lineMapper;
//    }
//
//    @Override
//    public void beforeStep(StepExecution stepExecution) {
//        LOGGER.debug("Lines Reader initialized => {}", stepExecution.getStartTime());
//    }
//
//    @Override
//    public ExitStatus afterStep(StepExecution stepExecution) {
//        LOGGER.debug("Lines Reader ended => {}", stepExecution.getEndTime());
//        return ExitStatus.COMPLETED;
//    }
//}
