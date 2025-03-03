package org.chomookun.arch4j.batch.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
public class JobListener implements JobExecutionListener {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final StopWatch stopWatch = new StopWatch();

    public static Object toObject() {
        return new JobListener();
    }

    @BeforeJob
    @Override
    public final void beforeJob(JobExecution jobExecution) {
        stopWatch.reset();
        stopWatch.start();
        log.info("=".repeat(80));
        log.info("| [START] JobExecution");
        log.info("| jobName: {}", jobExecution.getJobInstance().getJobName());
        log.info("| jobParameters: {}", jobExecution.getJobParameters());
        log.info("| startTime: {}", Optional.ofNullable(jobExecution.getStartTime()).map(DATE_TIME_FORMATTER::format).orElse(null));
        log.info("| status: {}", jobExecution.getStatus());
        log.info("=".repeat(80));
    }

    @AfterJob
    @Override
    public final void afterJob(JobExecution jobExecution) {
        stopWatch.stop();
        log.info("=".repeat(80));
        log.info("| [END] JobExecution");
        log.info("| jobName: {}", jobExecution.getJobInstance().getJobName());
        log.info("| jobParameters: {}", jobExecution.getJobParameters());
        log.info("| startTime: {}", Optional.ofNullable(jobExecution.getStartTime()).map(DATE_TIME_FORMATTER::format).orElse(null));
        log.info("| endTime: {}", Optional.ofNullable(jobExecution.getEndTime()).map(DATE_TIME_FORMATTER::format).orElse(null));
        log.info("| elapsedTime: {}", stopWatch.formatTime());
        log.info("| status: {}", jobExecution.getStatus());
        log.info("| exitStatus: {}", jobExecution.getExitStatus());
        log.info("=".repeat(80));
    }

}

