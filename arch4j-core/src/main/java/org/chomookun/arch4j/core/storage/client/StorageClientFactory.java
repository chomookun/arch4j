package org.chomookun.arch4j.core.storage.client;

import org.chomookun.arch4j.core.common.pbe.PbePropertiesUtil;
import org.chomookun.arch4j.core.storage.model.Storage;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySourcesPropertyResolver;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Properties;

public class StorageClientFactory {

    public static StorageClient getStorageClient(Storage storage) {
        StorageClientDefinition storageClientDefinition = StorageClientDefinitionRegistry.getStorageClientDefinition(storage.getClientType()).orElseThrow();
        try {
            Class<? extends StorageClient> clientType = storageClientDefinition.getClassType().asSubclass(StorageClient.class);
            Constructor<? extends StorageClient> constructor = clientType.getConstructor(Properties.class);
            Properties config = loadPropertiesFromString(storage.getClientProperties());
            return constructor.newInstance(config);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Storage client constructor not found: " + storage.getClientType(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties loadPropertiesFromString(String config) {
        Properties properties = PbePropertiesUtil.loadProperties(config);
        // applies spring properties resolver
        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addLast(new PropertiesPropertySource("storageClientProperties", properties));
        propertySources.addLast(new MapPropertySource("systemProperties", (Map)System.getProperties()));
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
