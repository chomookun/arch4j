package org.chomookun.arch4j.core.storage.client;

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
public class StorageClientDefinitionRegistry implements BeanPostProcessor {

    @Getter
    private static final List<StorageClientDefinition> storageClientDefinitions = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if(bean instanceof StorageClientDefinition) {
            storageClientDefinitions.add((StorageClientDefinition) bean);
        }
        return bean;
    }

    public static Optional<StorageClientDefinition> getStorageClientDefinition(String type) {
        return storageClientDefinitions.stream()
                .filter(item -> Objects.equals(item.getStorageClientId(), type))
                .findFirst();
    }

}
