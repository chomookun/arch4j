package org.chomookun.arch4j.core.execution.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.chomookun.arch4j.core.execution.entity.ExecutionEntity;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Builder
@Getter
public class Execution {

    private String executionId;

    private String taskName;

    @Setter
    private Status status;

    @Setter
    private Instant startedAt;

    @Setter
    private Instant updatedAt;

    @Setter
    private Instant endedAt;

    @Setter
    @Builder.Default
    private AtomicLong totalCount = new AtomicLong();

    @Setter
    @Builder.Default
    private AtomicLong successCount = new AtomicLong();

    @Setter
    @Builder.Default
    private AtomicLong failCount = new AtomicLong();

    @Setter
    private String message;

    public Duration getDuration() {
        if (startedAt == null) return null;
        if (endedAt == null) return Duration.between(startedAt, Instant.now());
        return Duration.between(startedAt, endedAt);
    }

    public enum Status {
        RUNNING,
        SUCCESS,
        FAILED,
        STOPPED
    }

    public static Execution from(ExecutionEntity executionEntity) {
        return Execution.builder()
                .executionId(executionEntity.getExecutionId())
                .taskName(executionEntity.getTaskName())
                .status(executionEntity.getStatus())
                .startedAt(executionEntity.getStartedAt())
                .updatedAt(executionEntity.getUpdatedAt())
                .endedAt(executionEntity.getEndedAt())
                .totalCount(new AtomicLong(Optional.ofNullable(executionEntity.getTotalCount()).orElse(0L)))
                .successCount(new AtomicLong(Optional.ofNullable(executionEntity.getSuccessCount()).orElse(0L)))
                .failCount(new AtomicLong(Optional.ofNullable(executionEntity.getFailCount()).orElse(0L)))
                .message(executionEntity.getMessage())
                .build();
    }

}
