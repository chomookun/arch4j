package org.chomookun.arch4j.core.execution.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.AbstractEnumConverter;
import org.chomookun.arch4j.core.execution.model.Execution;
import org.hibernate.annotations.Comment;

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
    @Comment("Execution ID")
    private String executionId;

    @Column(name = "task_name")
    @Comment("Task Name")
    private String taskName;

    @Column(name = "status", length = 16)
    @Convert(converter = StatusConverter.class)
    @Comment("Status")
    private Execution.Status status;

    @Column(name = "started_at")
    @Comment("Started At")
    private Instant startedAt;

    @Column(name = "updated_at")
    @Comment("Updated At")
    private Instant updatedAt;

    @Column(name = "ended_at")
    @Comment("Ended At")
    private Instant endedAt;

    @Column(name = "total_count")
    @Comment("Total Count")
    private Long totalCount;

    @Column(name = "success_count")
    @Comment("Success Count")
    private Long successCount;

    @Column(name = "fail_count")
    @Comment("Fail Count")
    private Long failCount;

    @Column(name = "message", length = Integer.MAX_VALUE)
    @Lob
    @Comment("Message")
    private String message;

    @Converter
    public static class StatusConverter extends AbstractEnumConverter<Execution.Status> {}

}
