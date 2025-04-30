package org.chomookun.arch4j.core.alarm.client;

import org.springframework.beans.factory.Aware;

public interface AlarmClientDefinition extends Aware {

    String getClientId();

    String getClientName();

    String getConfigTemplate();

    Class<? extends AlarmClient> getClassType();

}
