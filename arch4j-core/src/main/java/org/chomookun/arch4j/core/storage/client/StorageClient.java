package org.chomookun.arch4j.core.storage.client;

import lombok.Getter;
import org.chomookun.arch4j.core.storage.model.StorageResource;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public abstract class StorageClient {

    @Getter
    private final Properties properties;

    public StorageClient(Properties properties) {
        this.properties = properties;
    }

    public abstract List<StorageResource> getChildResources(String resourceId);

    public abstract StorageResource getParentResource(String resourceId);

    public abstract StorageResource getResource(String resourceId);

    public abstract StorageResource createFolderResource(String parentResourceId, String name);

    public abstract StorageResource createFileResource(String parentResourceId, String name, InputStream inputStream);

    public abstract void deleteFolderResource(String resourceId);

    public abstract void deleteFileResource(String resourceId);

}
