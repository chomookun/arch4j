package org.chomookun.arch4j.core.execution.dao;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.AbstractEnumConverter;
import org.chomookun.arch4j.core.execution.model.Execution;
import org.chomookun.arch4j.core.menu.model.Menu;

import java.time.Instant;

@Entity
@Table(name = "core_execution", indexes = {
        @Index(name = "ix_task_name", columnList = "task_name")
})
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExecutionEntity extends BaseEntity {

    @Id
    @Column(name = "execution_id", length = 32)
    private String executionId;

    @Column(name = "task_name", length = 128)
    private String taskName;

    @Column(name = "status", length = 16)
    @Convert(converter = StatusConverter.class)
    private Execution.Status status;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    @Column(name = "total_count")
    private Long totalCount;

    @Column(name = "success_count")
    private Long successCount;

    @Column(name = "fail_count")
    private Long failCount;

    @Column(name = "message")
    @Lob
    private String message;

    @Converter(autoApply = true)
    public static class StatusConverter extends AbstractEnumConverter<Execution.Status> {}

}
