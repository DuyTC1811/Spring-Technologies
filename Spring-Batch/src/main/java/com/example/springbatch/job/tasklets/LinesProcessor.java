package com.example.springbatch.job.tasklets;

import com.example.springbatch.entity.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
@Configuration
public class LinesProcessor implements Tasklet, StepExecutionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinesProcessor.class);

    private List<Line> lines;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        executionContext.get("lines");
        // Lấy value tử Map trả về List<Line> list
        this.lines = (List<Line>) executionContext.get("lines");
        LOGGER.info("Lines Processor initialized {}", stepExecution.getStartTime());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOGGER.info("Lines Processor ended {}", stepExecution.getEndTime());
        return ExitStatus.COMPLETED;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        for (Line line : lines) {
            long age = ChronoUnit.YEARS.between(line.getDob(), LocalDate.now());
            LOGGER.info("Calculated age {} for line {}", age, line);
            line.setAge(age);
        }
        return RepeatStatus.FINISHED;
    }
}
