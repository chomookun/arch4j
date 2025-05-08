package org.chomookun.arch4j.core.verification.client;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Lazy(false)
public class VerifierClientFactoryRegistry implements BeanPostProcessor {

    @Getter
    private static final List<VerifierClientFactory> factories = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if(bean instanceof VerifierClientFactory) {
            factories.add((VerifierClientFactory) bean);
        }
        return bean;
    }

    public static Optional<VerifierClientFactory> getFactory(VerifierClientDefinition verifierDefinition) {
        Class<? extends VerifierClient> typeClass = verifierDefinition.getClassType();
        return factories.stream()
                .filter(item -> Objects.equals(item.getTypeClass(), typeClass))
                .findFirst();
    }

}
