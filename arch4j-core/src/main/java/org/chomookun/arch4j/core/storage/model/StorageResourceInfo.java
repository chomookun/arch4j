package org.chomookun.arch4j.core.storage.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class StorageResourceInfo {

    private String resourceId;

    private String name;

    private StorageResource.Type type;

    private long length;

    private Instant lastModified;

    public static StorageResourceInfo from(StorageResource storageResource) {
        return StorageResourceInfo.builder()
                .resourceId(storageResource.getResourceId())
                .name(storageResource.getName())
                .type(storageResource.getType())
                .length(storageResource.getLength())
                .lastModified(storageResource.getLastModified())
                .build();
    }
}
