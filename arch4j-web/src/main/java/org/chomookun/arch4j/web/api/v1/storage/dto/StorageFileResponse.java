package org.chomookun.arch4j.web.api.v1.storage.dto;

import lombok.*;
import org.chomookun.arch4j.core.storage.model.StorageFile;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StorageFileResponse {

    private String fileId;

    private String targetType;

    private String targetId;

    private String filename;

    private Long size;

    private Instant createdAt;

    private String storageId;

    private String resourceId;

    public static StorageFileResponse from(StorageFile storageObject) {
        return StorageFileResponse.builder()
                .fileId(storageObject.getFileId())
                .targetType(storageObject.getTargetType())
                .targetId(storageObject.getTargetId())
                .filename(storageObject.getFilename())
                .size(storageObject.getSize())
                .createdAt(storageObject.getCreatedAt())
                .storageId(storageObject.getStorageId())
                .resourceId(storageObject.getResourceId())
                .build();
    }

}
