package org.chomookun.arch4j.core.storage.client.s3;

import org.chomookun.arch4j.core.storage.client.StorageClient;
import org.chomookun.arch4j.core.storage.client.StorageClientDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

@Component
@Lazy(false)
public class S3StorageClientDefinition implements StorageClientDefinition {

    @Override
    public String getClientType() {
        return "S3";
    }

    @Override
    public String getName() {
        return "AWS S3 Storage (Not available yet)";
    }

    @Override
    public Class<? extends StorageClient> getClassType() {
        return S3StorageClient.class;
    }

    @Override
    public String getPropertiesTemplate() {
        StringJoiner template = new StringJoiner("\n");
        template.add("bucket=[bucket]");
        return template.toString();
    }

}
