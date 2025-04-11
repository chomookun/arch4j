package org.chomookun.arch4j.core.storage.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class StorageResourceSummary {

    private String storageId;

    private String resourceId;

    private StorageResourceInfo parentStorageResource;

    private List<StorageResourceInfo> childStorageResources;

}
