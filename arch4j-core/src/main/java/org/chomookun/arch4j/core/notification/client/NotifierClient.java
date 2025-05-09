package org.chomookun.arch4j.core.notification.client;

import lombok.Getter;

import java.util.Map;
import java.util.Properties;

public abstract class NotifierClient {

    @Getter
    private final Properties properties;

    public NotifierClient(Properties properties) {
        this.properties = properties;
    }

    public abstract void sendMessage(String subject, String content, String receiver, Map<String,Object> option);

}
