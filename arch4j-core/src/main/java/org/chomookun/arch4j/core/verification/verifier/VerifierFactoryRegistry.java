package org.chomookun.arch4j.core.verification.verifier;

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
public class VerifierFactoryRegistry implements BeanPostProcessor {

    @Getter
    private static final List<VerifierFactory> verifierFactories = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if(bean instanceof VerifierFactory) {
            verifierFactories.add((VerifierFactory) bean);
        }
        return bean;
    }

    public static Optional<VerifierFactory> getVerifierFactory(VerifierDefinition verifierDefinition) {
        Class<? extends Verifier> typeClass = verifierDefinition.getTypeClass();
        return verifierFactories.stream()
                .filter(item -> Objects.equals(item.getTypeClass(), typeClass))
                .findFirst();
    }

}
