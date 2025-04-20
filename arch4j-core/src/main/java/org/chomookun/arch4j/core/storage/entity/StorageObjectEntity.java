package org.chomookun.arch4j.core.storage.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import java.time.Instant;

@Entity
@Table(name = "core_storage_object")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StorageObjectEntity extends BaseEntity {

    @Id
    @Column(name = "object_id")
    private String objectId;

    @Column(name = "ref_type", length = 32, nullable = false)
    private String refType;

    @Column(name = "ref_id", length = 128, nullable = false)
    private String refId;

    @Column(name = "filename")
    private String filename;

    @Column(name = "size")
    private Long size;

    @Column(name = "last_modified")
    private Instant lastModified;

    @Column(name = "storage_id", length = 32)
    private String storageId;

    @Column(name = "resource_id")
    private String resourceId;

}
