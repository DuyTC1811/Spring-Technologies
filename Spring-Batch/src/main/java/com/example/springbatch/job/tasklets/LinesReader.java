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
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
@Configuration
public class LinesReader implements Tasklet, StepExecutionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinesReader.class);

    private List<Line> lines;
    private FileUtils fileUtils;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        lines = new ArrayList<>();
        fileUtils = new FileUtils("input/tasklets-vs-chunks.csv");
        LOGGER.info("Lines Reader initialized {}", stepExecution.getStartTime());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        fileUtils.closeReader();
        // Khi đọc Data sau đó Put vào map để các Bước tiếp theo sử dụng data đó
        stepExecution.getJobExecution().getExecutionContext().put("lines", this.lines);
        LOGGER.info("Lines Reader ended {}", stepExecution.getEndTime());
        return ExitStatus.COMPLETED;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        Line line = fileUtils.readLine();
        while (line != null) {
            lines.add(line);
            LOGGER.info("Read line: {}", line);
            line = fileUtils.readLine();
        }
        return RepeatStatus.FINISHED;
    }
}
