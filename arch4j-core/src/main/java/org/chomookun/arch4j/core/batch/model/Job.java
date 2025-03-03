package org.chomookun.arch4j.core.batch.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;

import java.time.LocalDateTime;

@Builder
@Getter
public class Job {

    private String jobName;

    private LocalDateTime lastStartTime;

    private LocalDateTime lastEndTime;

    private BatchStatus lastBatchStatus;

    private ExitStatus lastExitStatus;

    private long instanceCount;

}
