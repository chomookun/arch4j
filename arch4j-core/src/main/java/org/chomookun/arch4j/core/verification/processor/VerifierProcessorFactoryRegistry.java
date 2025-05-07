package org.chomookun.arch4j.core.verification.processor;

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
public class VerifierProcessorFactoryRegistry implements BeanPostProcessor {

    @Getter
    private static final List<VerifierProcessorFactory> factories = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if(bean instanceof VerifierProcessorFactory) {
            factories.add((VerifierProcessorFactory) bean);
        }
        return bean;
    }

    public static Optional<VerifierProcessorFactory> getFactory(VerifierProcessorDefinition verifierDefinition) {
        Class<? extends VerifierProcessor> typeClass = verifierDefinition.getTypeClass();
        return factories.stream()
                .filter(item -> Objects.equals(item.getTypeClass(), typeClass))
                .findFirst();
    }

}
