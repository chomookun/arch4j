package org.chomookun.arch4j.core.verification.verifier;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class VerifierDefinitionRegistry implements BeanPostProcessor {

    @Getter
    private static final List<VerifierDefinition> verifierDefinitions = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if(bean instanceof VerifierDefinition) {
            verifierDefinitions.add((VerifierDefinition) bean);
        }
        return bean;
    }

    public static Optional<VerifierDefinition> getVerifierDefinition(String verifierType) {
        return verifierDefinitions.stream()
                .filter(item -> Objects.equals(item.getType(), verifierType))
                .findFirst();
    }

}
