package org.chomookun.arch4j.core.notification.client;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class NotificationClientDefinitionRegistry implements BeanPostProcessor {

    @Getter
    private static final List<NotificationClientDefinition> notificationClientDefinitions = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if(bean instanceof NotificationClientDefinition) {
            notificationClientDefinitions.add((NotificationClientDefinition) bean);
        }
        return bean;
    }

    public static Optional<NotificationClientDefinition> getNotificationClientDefinition(String notificationClientId) {
        return notificationClientDefinitions.stream()
                .filter(item -> Objects.equals(item.getClientId(), notificationClientId))
                .findFirst();
    }

}
