package org.chomookun.arch4j.core.batch.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.batch.core.*;

import java.time.LocalDateTime;

@Builder
@Getter
public class JobExecution {

    private String jobName;

    private long instanceId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private BatchStatus status;

    private ExitStatus exitStatus;

}
