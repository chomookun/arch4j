package org.chomookun.arch4j.core.storage.client;

import org.springframework.beans.factory.Aware;

public interface StorageClientDefinition extends Aware {

    String getStorageClientId();

    String getStorageClientName();

    String getStorageClientConfigTemplate();

    Class<? extends StorageClient> getClassType();

}
