package org.chomookun.arch4j.core.storage.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
public class StorageResourceInfo {

    @Setter
    private String storageId;

    private String resourceId;

    private String filename;

    private long size;

    private Instant lastModified;

    private StorageResource.Type type;

    @Setter
    private StorageResourceInfo parentStorageResource;

    @Setter
    private List<StorageResourceInfo> childStorageResources;

    public static StorageResourceInfo from(StorageResource storageResource) {
        return StorageResourceInfo.builder()
                .resourceId(storageResource.getResourceId())
                .filename(storageResource.getFilename())
                .size(storageResource.getSize())
                .lastModified(storageResource.getLastModified())
                .type(storageResource.getType())
                .build();
    }

}
