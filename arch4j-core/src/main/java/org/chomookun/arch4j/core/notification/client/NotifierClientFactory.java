package org.chomookun.arch4j.core.notification.client;

import org.chomookun.arch4j.core.notification.model.Notifier;
import org.chomookun.arch4j.core.common.pbe.PbePropertiesUtil;

import java.lang.reflect.Constructor;
import java.util.Properties;

public class NotifierClientFactory {

    public static NotifierClient getNotificationClient(Notifier notification) {
        NotifierClientDefinition notificationClientDefinition = NotifierClientDefinitionRegistry.getNotifierDefinition(notification.getClientType()).orElseThrow();
        try {
            Class<? extends NotifierClient> clientType = notificationClientDefinition.getTypeClass().asSubclass(NotifierClient.class);
            Constructor<? extends NotifierClient> constructor = clientType.getConstructor(Properties.class);
            Properties config = loadPropertiesFromString(notification.getClientConfig());
            return constructor.newInstance(config);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Notification client constructor not found: " + notification.getClientType(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties loadPropertiesFromString(String config) {
        return PbePropertiesUtil.loadProperties(config);
    }

}
