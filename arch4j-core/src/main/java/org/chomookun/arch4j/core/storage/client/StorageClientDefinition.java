package org.chomookun.arch4j.core.storage.client;

import org.springframework.beans.factory.Aware;

public interface StorageClientDefinition extends Aware {

    String getClientType();

    String getName();

    Class<? extends StorageClient> getClassType();

    String getPropertiesTemplate();

}
