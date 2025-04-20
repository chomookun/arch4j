package org.chomookun.arch4j.core.storage.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.BooleanConverter;

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

    @Column(name = "storage_client_id", length = 32)
    private String storageClientId;

    @Column(name = "storage_client_config", length = Integer.MAX_VALUE)
    @Lob
    private String storageClientConfig;

    @Column(name = "sanitize_enabled", length = 1)
    @Convert(converter = BooleanConverter.class)
    private boolean sanitizeEnabled;

}
