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
public class NotifierClientDefinitionRegistry implements BeanPostProcessor {

    @Getter
    private static final List<NotifierClientDefinition> notifierClientDefinitions = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if(bean instanceof NotifierClientDefinition) {
            notifierClientDefinitions.add((NotifierClientDefinition) bean);
        }
        return bean;
    }

    public static Optional<NotifierClientDefinition> getNotifierDefinition(String notifierType) {
        return notifierClientDefinitions.stream()
                .filter(item -> Objects.equals(item.getClientType(), notifierType))
                .findFirst();
    }

}
