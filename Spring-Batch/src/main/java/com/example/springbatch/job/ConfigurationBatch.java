package com.example.springbatch.job;

import com.example.springbatch.entity.Customer;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
public class ConfigurationBatch {
    @Qualifier("taskExecutor")
    private final TaskExecutor taskExecutor;
    private final SqlSessionFactory sqlSessionFactory;
    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public ConfigurationBatch(
            @Qualifier("taskExecutor") TaskExecutor taskExecutor,
            SqlSessionFactory sqlSessionFactory,
            JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory) {
        this.taskExecutor = taskExecutor;
        this.sqlSessionFactory = sqlSessionFactory;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean // Đọc từ file CSV
    public FlatFileItemReader<Customer> reader() {
        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/static/customers.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Customer> lineMapper() {                                                                             // map từng dòng
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");                                                                                    // chỉ da chỗ phân cách bằng giấu phẩy hoặc các giấu khác dùng để phân cách
        lineTokenizer.setStrict(true);
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");            // những field cần map

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public CustomerProcessor processor() {
        return new CustomerProcessor();                                                     // gọi đến bộ sử lý trong đó mình có thể sử lý logic mà ta muốn
    }

    @Bean
    public MyBatisBatchItemWriter<Customer> writer() {                                     // cấu hình để dùng reppository
        MyBatisBatchItemWriter<Customer> writer = new MyBatisBatchItemWriter<>();
        writer.setSqlSessionFactory(sqlSessionFactory);                                    // set repository
        writer.setStatementId("insert");                                                   // các phương thức ta ta dùng ví dụ "save" "delete" "getAll"
        return writer;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory
                .get("csv-step")
                .<Customer, Customer>chunk(500)                 // mỗi lần sử lý 10 bản ghi
                .reader(reader())                                        // Gọi phương thức "reder"
                .processor(processor())                                  // Gọi phương thức "processor"
                .writer(writer())                                        // Gọi phương thức ghi data vào data base
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                System.out.println("before job");
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                System.out.println("after job");
            }
        };
    }

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("importCustomers")
                .flow(step1())
                .end()
                .build();
    }
}
