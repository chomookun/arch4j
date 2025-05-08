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
public class VerifierClientDefinitionRegistry implements BeanPostProcessor {

    @Getter
    private static final List<VerifierClientDefinition> definitions = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if(bean instanceof VerifierClientDefinition) {
            definitions.add((VerifierClientDefinition) bean);
        }
        return bean;
    }

    public static Optional<VerifierClientDefinition> getDefinition(String verifierType) {
        return definitions.stream()
                .filter(item -> Objects.equals(item.getClientType(), verifierType))
                .findFirst();
    }

}
