package org.chomookun.arch4j.core.notification.client;

import org.springframework.beans.factory.Aware;

public interface NotifierClientDefinition extends Aware {

    String getClientType();

    String getName();

    Class<? extends NotifierClient> getTypeClass();

    String getConfigTemplate();

}
