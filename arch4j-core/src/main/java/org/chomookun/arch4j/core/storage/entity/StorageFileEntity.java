package org.chomookun.arch4j.core.storage.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import java.time.Instant;

@Entity
@Table(
        name = "core_storage_file",
        indexes = {
                @Index(name = "idx_core_storage_file_target", columnList = "target_type, target_id")
        }
)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StorageFileEntity extends BaseEntity {

    @Id
    @Column(name = "file_id", length = 32)
    private String fileId;

    @Column(name = "target_type", length = 32)
    private String targetType;

    @Column(name = "target_id", length = 128)
    private String targetId;

    @Column(name = "filename")
    private String filename;

    @Column(name = "size")
    private Long size;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "storage_id", length = 32)
    private String storageId;

    @Column(name = "resource_id")
    private String resourceId;

}
