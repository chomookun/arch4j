package org.chomookun.arch4j.core.storage.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

@Entity
@Table(name = "core_storage")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StorageEntity extends BaseEntity {

    @Id
    @Column(name = "storage_id", length = 32)
    private String storageId;

    @Column(name = "name")
    private String name;

    @Column(name = "client_type", length = 32)
    private String clientType;

    @Column(name = "client_properties", length = Integer.MAX_VALUE)
    @Lob
    private String clientProperties;

}
