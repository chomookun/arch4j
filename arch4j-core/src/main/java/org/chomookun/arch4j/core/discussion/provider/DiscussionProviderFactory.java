package org.chomookun.arch4j.core.discussion.provider;

import org.chomookun.arch4j.core.common.pbe.PbePropertiesUtil;
import org.chomookun.arch4j.core.discussion.model.Discussion;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;

import java.lang.reflect.Constructor;
import java.util.Properties;

public class DiscussionProviderFactory {

    public static DiscussionProvider getDiscussionProvider(Discussion discussion) {
        DiscussionProviderDefinition discussionProviderDefinition = DiscussionProviderDefinitionRegistry.getDiscussionProviderDefinition(discussion.getDiscussionProviderId()).orElseThrow();
        try {
            Class<? extends DiscussionProvider> clientType = discussionProviderDefinition.getClassType().asSubclass(DiscussionProvider.class);
            Constructor<? extends DiscussionProvider> constructor = clientType.getConstructor(Properties.class);
            Properties config = loadPropertiesFromString(discussion.getDiscussionProviderConfig());
            return constructor.newInstance(config);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Discussion provider constructor not found: " + discussion.getDiscussionId(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties loadPropertiesFromString(String config) {
        Properties properties = PbePropertiesUtil.loadProperties(config);
        // applies spring properties resolver
        MutablePropertySources propertySources = new MutablePropertySources();
        PropertySourcesPropertyResolver resolver = new PropertySourcesPropertyResolver(propertySources);
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            if (value != null) {
                value = resolver.resolveRequiredPlaceholders(value);
            }
            properties.setProperty(key, value);
        }
        return properties;
    }

}
