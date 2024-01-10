package org.oopscraft.arch4j.core.alarm.client;

import org.springframework.beans.factory.Aware;

public interface AlarmClientDefinition extends Aware {

    String getId();

    String getName();

    Class<? extends AlarmClient> getType();

    String getConfigTemplate();

}
