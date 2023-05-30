package com.example.springbatch.job.tasklets;

import com.example.springbatch.entity.Line;
import com.example.springbatch.utils.FileUtils;
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

import java.util.List;
@Configuration
public class LinesWriter implements Tasklet, StepExecutionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinesWriter.class);
    private List<Line> lines;
    private FileUtils fu;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        this.lines = (List<Line>) executionContext.get("lines");
        fu = new FileUtils("output.csv");
        LOGGER.info("Lines Writer initialized => {}", stepExecution.getStartTime());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        fu.closeWriter();
        LOGGER.info("Lines Writer ended => {}", stepExecution.getEndTime());
        return ExitStatus.COMPLETED;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        for (Line line : lines) {
            fu.writeLine(line);
            LOGGER.debug("Wrote line {}", line.toString());
        }
        return RepeatStatus.FINISHED;
    }
}
