package org.chomookun.arch4j.core.storage.client;

import org.chomookun.arch4j.core.alarm.client.AlarmClient;
import org.springframework.beans.factory.Aware;
import org.springframework.core.io.Resource;

public interface StorageClientDefinition extends Aware {

    String getStorageClientId();

    String getStorageClientName();

    String getStorageClientConfigTemplate();

    Class<? extends StorageClient> getClassType();

}
