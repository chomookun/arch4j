package org.chomookun.arch4j.core.notification.client;

import org.springframework.beans.factory.Aware;

import java.util.List;
import java.util.Map;

public interface NotificationClientDefinition extends Aware {

    String getClientId();

    String getName();

    String getConfigTemplate();

    Class<? extends NotificationClient> getClassType();

}
