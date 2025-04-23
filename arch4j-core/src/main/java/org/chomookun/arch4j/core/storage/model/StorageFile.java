package org.chomookun.arch4j.core.storage.model;

import lombok.*;
import org.chomookun.arch4j.core.storage.entity.StorageFileEntity;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StorageFile {

    private String fileId;

    private String targetType;

    private String targetId;

    private String filename;

    private Long size;

    private Instant createdAt;

    private String storageId;

    private String resourceId;

    public static StorageFile from(StorageFileEntity storageObjectEntity) {
        return StorageFile.builder()
                .fileId(storageObjectEntity.getFileId())
                .targetType(storageObjectEntity.getTargetType())
                .targetId(storageObjectEntity.getTargetId())
                .filename(storageObjectEntity.getFilename())
                .size(storageObjectEntity.getSize())
                .createdAt(storageObjectEntity.getCreatedAt())
                .storageId(storageObjectEntity.getStorageId())
                .resourceId(storageObjectEntity.getResourceId())
                .build();
    }

}
