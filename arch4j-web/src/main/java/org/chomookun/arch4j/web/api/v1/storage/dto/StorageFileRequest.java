package org.chomookun.arch4j.web.api.v1.storage.dto;

import lombok.*;
import org.chomookun.arch4j.core.storage.model.StorageFile;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StorageFileRequest {

    private String fileId;

    private String targetType;

    private String targetId;

    private String filename;

    private Long size;

    private Instant createdAt;

    private String storageId;

    private String resourceId;

    public StorageFile toModel() {
        return StorageFile.builder()
                .fileId(fileId)
                .targetType(targetType)
                .targetId(targetId)
                .filename(filename)
                .size(size)
                .createdAt(createdAt)
                .storageId(storageId)
                .resourceId(resourceId)
                .build();
    }

}
