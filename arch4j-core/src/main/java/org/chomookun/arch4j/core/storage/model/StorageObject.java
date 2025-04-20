package org.chomookun.arch4j.core.storage.model;

import lombok.*;
import org.chomookun.arch4j.core.storage.entity.StorageObjectEntity;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StorageObject {

    private String objectId;

    private String refType;

    private String refId;

    private String filename;

    private Long size;

    private Instant lastModified;

    private String storageId;

    private String resourceId;

    public static StorageObject from(StorageObjectEntity storageObjectEntity) {
        return StorageObject.builder()
                .objectId(storageObjectEntity.getObjectId())
                .refType(storageObjectEntity.getRefType())
                .refId(storageObjectEntity.getRefId())
                .filename(storageObjectEntity.getFilename())
                .size(storageObjectEntity.getSize())
                .lastModified(storageObjectEntity.getLastModified())
                .storageId(storageObjectEntity.getStorageId())
                .resourceId(storageObjectEntity.getResourceId())
                .build();
    }

}
