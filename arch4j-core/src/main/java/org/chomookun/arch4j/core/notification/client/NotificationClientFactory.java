package org.chomookun.arch4j.core.notification.client;

import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.common.pbe.PbePropertiesUtil;

import java.lang.reflect.Constructor;
import java.util.Properties;

public class NotificationClientFactory {

    public static NotificationClient getNotificationClient(Notification notification) {
        NotificationClientDefinition notificationClientDefinition = NotificationClientDefinitionRegistry.getNotificationClientDefinition(notification.getClientId()).orElseThrow();
        try {
            Class<? extends NotificationClient> clientType = notificationClientDefinition.getClassType().asSubclass(NotificationClient.class);
            Constructor<? extends NotificationClient> constructor = clientType.getConstructor(Properties.class);
            Properties config = loadPropertiesFromString(notification.getClientConfig());
            return constructor.newInstance(config);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Notification client constructor not found: " + notification.getClientId(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties loadPropertiesFromString(String config) {
        return PbePropertiesUtil.loadProperties(config);
    }

}
