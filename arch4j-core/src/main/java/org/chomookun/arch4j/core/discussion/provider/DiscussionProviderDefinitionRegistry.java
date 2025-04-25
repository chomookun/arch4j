package org.chomookun.arch4j.core.discussion.provider;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class DiscussionProviderDefinitionRegistry implements BeanPostProcessor {

    @Getter
    private static final List<DiscussionProviderDefinition> discussionProviderDefinitions = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if(bean instanceof DiscussionProviderDefinition) {
            discussionProviderDefinitions.add((DiscussionProviderDefinition) bean);
        }
        return bean;
    }

    public static Optional<DiscussionProviderDefinition> getDiscussionProviderDefinition(String type) {
        return discussionProviderDefinitions.stream()
                .filter(item -> Objects.equals(item.getDiscussionProviderId(), type))
                .findFirst();
    }

}
