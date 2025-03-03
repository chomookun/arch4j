package org.chomookun.arch4j.core.batch.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;

import java.time.LocalDateTime;

@Builder
@Getter
public class JobInstance {

    private String jobName;

    private long instanceId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private BatchStatus batchStatus;

    private ExitStatus exitStatus;

    private long executionCount;

}
