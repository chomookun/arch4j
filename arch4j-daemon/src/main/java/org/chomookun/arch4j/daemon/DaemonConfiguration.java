package org.chomookun.arch4j.daemon;


import org.chomookun.arch4j.core.CoreConfiguration;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.Optional;
import java.util.Properties;

@Configuration
@Import(CoreConfiguration.class)
@ConfigurationPropertiesScan
@ComponentScan(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@EnableAutoConfiguration
@EnableScheduling
@EnableCaching
public class DaemonConfiguration implements EnvironmentPostProcessor, WebMvcConfigurer {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Resource resource = new DefaultResourceLoader().getResource("classpath:daemon.yml");
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource);
        factory.afterPropertiesSet();
        Properties properties = Optional.ofNullable(factory.getObject()).orElseThrow(RuntimeException::new);
        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("daemon", properties);
        environment.getPropertySources().addLast(propertiesPropertySource);
    }

    @Configuration
    @EnableWebSocketMessageBroker
    static class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/ws")
                    .setAllowedOriginPatterns("*");
        }
        @Override
        public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
            registry.setMessageSizeLimit(2 * 1024 * 1024) // 2MB (default: 64KB)
                    .setSendBufferSizeLimit(4 * 1024 * 1024) // 4MB (default: 512KB)
                    .setTimeToFirstMessage(10_000); // 10 second first message (default: 5s)
        }
    }

}
