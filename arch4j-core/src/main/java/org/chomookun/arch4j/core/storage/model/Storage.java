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

    private String storageClientId;

    private String storageClientConfig;

    private boolean sanitizeEnabled;

    /**
     * Factory method from storage entity
     * @param storageEntity storage entity
     * @return storage
     */
    public static Storage from(StorageEntity storageEntity) {
        return Storage.builder()
                .storageId(storageEntity.getStorageId())
                .name(storageEntity.getName())
                .storageClientId(storageEntity.getStorageClientId())
                .storageClientConfig(storageEntity.getStorageClientConfig())
                .sanitizeEnabled(storageEntity.isSanitizeEnabled())
                .build();
    }

}
