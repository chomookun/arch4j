package org.chomookun.arch4j.core.storage.client.s3;


import org.chomookun.arch4j.core.storage.client.StorageClient;
import org.chomookun.arch4j.core.storage.model.StorageResource;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * todo
 */
public class S3StorageClient extends StorageClient {

    private final S3Client s3Client;

    private final String bucket;

    public S3StorageClient(Properties properties) {
        super(properties);
        this.s3Client = S3Client.builder()
                .build();
        this.bucket = properties.getProperty("bucket");
    }

    @Override
    public void initializeRootFolder() {

    }

    @Override
    public List<StorageResource> getChildResources(String resourceId) {
        return List.of();
    }

    @Override
    public StorageResource getParentResource(String resourceId) {
        return null;
    }

    @Override
    public StorageResource getResource(String resourceId) {
        return null;
    }

    @Override
    public StorageResource createFolderResource(String parentResourceId, String name) {
        return null;
    }

    @Override
    public StorageResource createFileResource(String parentResourceId, String name, InputStream inputStream) {
        return null;
    }

    @Override
    public void deleteFolderResource(String resourceId) {

    }

    @Override
    public void deleteFileResource(String resourceId) {

    }

}