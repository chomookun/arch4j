package org.chomookun.arch4j.core.storage.client.file;

import org.chomookun.arch4j.core.storage.client.StorageClient;
import org.chomookun.arch4j.core.storage.client.StorageClientDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

@Component
@Lazy(false)
public class FileStorageClientDefinition implements StorageClientDefinition {

    @Override
    public String getStorageClientId() {
        return "FILE";
    }

    @Override
    public String getStorageClientName() {
        return "Local File System";
    }

    @Override
    public String getStorageClientConfigTemplate() {
        StringJoiner template = new StringJoiner("\n");
        template.add("location=[root directory]");
        return template.toString();
    }

    @Override
    public Class<? extends StorageClient> getClassType() {
        return FileStorageClient.class;
    }

}
