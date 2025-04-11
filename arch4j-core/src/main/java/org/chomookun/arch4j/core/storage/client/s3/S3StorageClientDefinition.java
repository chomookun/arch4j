package org.chomookun.arch4j.core.storage.client.s3;

import org.chomookun.arch4j.core.storage.client.StorageClient;
import org.chomookun.arch4j.core.storage.client.StorageClientDefinition;
import org.chomookun.arch4j.core.storage.client.file.FileStorageClient;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

@Component
public class S3StorageClientDefinition implements StorageClientDefinition {

    @Override
    public String getStorageClientId() {
        return "S3";
    }

    @Override
    public String getStorageClientName() {
        return "AWS S3 Storage (Not available yet)";
    }

    @Override
    public String getStorageClientConfigTemplate() {
        StringJoiner template = new StringJoiner("\n");
        template.add("bucket=[bucket]");
        return template.toString();
    }

    @Override
    public Class<? extends StorageClient> getClassType() {
        return S3StorageClient.class;
    }

}
