package org.chomookun.arch4j.core.storage.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.storage.entity.StorageEntity;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Storage extends BaseModel {

    @NotNull
    private String storageId;

    @NotBlank
    private String name;

    private String clientType;

    private String clientProperties;

    public static Storage from(StorageEntity storageEntity) {
        return Storage.builder()
                .storageId(storageEntity.getStorageId())
                .name(storageEntity.getName())
                .clientType(storageEntity.getClientType())
                .clientProperties(storageEntity.getClientProperties())
                .build();
    }

}
