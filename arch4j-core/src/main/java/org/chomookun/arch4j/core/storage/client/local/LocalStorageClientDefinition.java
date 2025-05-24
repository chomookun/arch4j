package org.chomookun.arch4j.core.storage.client.local;

import org.chomookun.arch4j.core.storage.client.StorageClient;
import org.chomookun.arch4j.core.storage.client.StorageClientDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

@Component
@Lazy(false)
public class LocalStorageClientDefinition implements StorageClientDefinition {

    @Override
    public String getClientType() {
        return "LOCAL";
    }

    @Override
    public String getName() {
        return "Local File System";
    }

    @Override
    public Class<? extends StorageClient> getClassType() {
        return LocalStorageClient.class;
    }

    @Override
    public String getPropertiesTemplate() {
        StringJoiner template = new StringJoiner("\n");
        template.add("location=[root directory]");
        return template.toString();
    }

}
