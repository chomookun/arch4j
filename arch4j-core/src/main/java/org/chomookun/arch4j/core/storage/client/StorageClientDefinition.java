package org.chomookun.arch4j.core.storage.client;

import org.springframework.beans.factory.Aware;

public interface StorageClientDefinition extends Aware {

    String getStorageClientId();

    String getName();

    String getConfigTemplate();

    Class<? extends StorageClient> getClassType();

}
