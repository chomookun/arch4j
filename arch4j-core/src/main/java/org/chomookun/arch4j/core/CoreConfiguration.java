package org.chomookun.arch4j.core;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.h2.tools.Server;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.chomookun.arch4j.core.message.MessageService;
import org.chomookun.arch4j.core.message.MessageSource;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.concurrent.DelegatingSecurityContextScheduledExecutorService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManager;
import redis.embedded.RedisServer;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;

@Slf4j
@Configuration
@ComponentScan(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@EnableAutoConfiguration(
        exclude = {
                MessageSourceAutoConfiguration.class
        }
)
@ConfigurationPropertiesScan
@EnableEncryptableProperties
@EnableConfigurationProperties
@EnableJpaRepositories
@EntityScan
@EnableTransactionManagement
@MapperScan(
        annotationClass = Mapper.class,
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@EnableScheduling
@EnableCaching
public class CoreConfiguration implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // load default core config
        Resource resource = new DefaultResourceLoader().getResource("classpath:core.yml");
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource);
        factory.afterPropertiesSet();
        Properties properties = Optional.ofNullable(factory.getObject()).orElseThrow(RuntimeException::new);
        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("core", properties);
        environment.getPropertySources().addLast(propertiesPropertySource);
        // overrides debug log level
        if("debug".equalsIgnoreCase(environment.getProperty("logging.level.root"))) {
            environment.getSystemProperties().put("logging.level.org.hibernate.SQL", "DEBUG");
            environment.getSystemProperties().put("logging.level.org.hibernate.type.descriptor.sql.BasicBinder", "TRACE");
            environment.getSystemProperties().put("logging.level.jdbc.resultsettable", "DEBUG");
        }
        // exchanges jdbc-url to cluster if embedded
        exchangeJdbcUrlToClusterIfEmbedded(environment);
    }

    @Bean
    public StandardPBEStringEncryptor jasyptEncryptorBean(ConfigurableEnvironment environment) {
        StandardPBEStringEncryptor pbeEncryptor = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig pbeConfig = new EnvironmentStringPBEConfig();
        String password = environment.getProperty("jasypt.encryptor.password");
        pbeConfig.setPassword(password);
        pbeEncryptor.setConfig(pbeConfig);
        return pbeEncryptor;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.messages")
    public MessageSourceProperties messageSourceProperties() {
        return new MessageSourceProperties();
    }

    @Bean
    public MessageSource messageSource(MessageSourceProperties messageProperties, MessageService messageService) {
        MessageSource messageSource = new MessageSource(messageService);
        // basename
        List<String> basenames = messageProperties.getBasename();
        if (basenames != null && !basenames.isEmpty()) {
            String[] basenameArray = basenames.stream()
                    .map(name -> "classpath*:" + name.trim())
                    .toArray(String[]::new);
            messageSource.setBasenames(basenameArray);
        }
        // encoding
        if (messageProperties.getEncoding() != null) {
            messageSource.setDefaultEncoding(messageProperties.getEncoding().name());
        }
        messageSource.setFallbackToSystemLocale(messageProperties.isFallbackToSystemLocale());
        Duration cacheDuration = messageProperties.getCacheDuration();
        if (cacheDuration != null) {
            messageSource.setCacheMillis(cacheDuration.toMillis());
        }
        messageSource.setAlwaysUseMessageFormat(messageProperties.isAlwaysUseMessageFormat());
        messageSource.setUseCodeAsDefaultMessage(messageProperties.isUseCodeAsDefaultMessage());
        return messageSource;
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CaffeineCacheManager cacheManager() {
        return new CaffeineCacheManager();
    }

    @Bean
    public KeyGenerator simpleKeyGenerator() {
        return new SimpleKeyGenerator();
    }

    /**
     * exchanges h2 jdbc-url to cluster if embedded
     * @param environment environment
     */
    void exchangeJdbcUrlToClusterIfEmbedded(ConfigurableEnvironment environment) {
        String jdbcUrl = environment.getProperty("spring.datasource.hikari.jdbc-url");
        String jdbcUrlCluster = environment.getProperty("spring.datasource.hikari.jdbc-url-cluster");
        if (Objects.requireNonNull(jdbcUrl).contains(":h2:mem")) {
            try (Socket socket = new Socket("127.0.0.1", 9092)) {
                // exchanges jdbc-url to cluster
                environment.getSystemProperties().put("spring.datasource.hikari.jdbc-url", jdbcUrlCluster);
            } catch (IOException ignore) {}
        }
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server inMemoryH2DatabaseServer(DataSource dataSource, ConfigurableEnvironment environment) throws SQLException {
        if (EmbeddedDatabaseConnection.isEmbedded(dataSource)) {
            return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
        }
        return null;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RedisServer redisServer(ConfigurableEnvironment environment) throws IOException {
        String host = environment.getProperty("spring.redis.host");
        if ("localhost".equals(host) || "127.0.0.1".equals(host)) {
            int port = Integer.parseInt(Objects.requireNonNull(environment.getProperty("spring.redis.port")));
            boolean portInUse = false;
            try (Socket socket = new Socket(host, port)) {
                portInUse = true;
            } catch (IOException e) {
                portInUse = false;
            }
            if (portInUse) {
                log.info("redisServer - Port {} is already in use.", port);
                return null;
            }
            // settings
            return new RedisServer(port);
        }
        return null;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    @Configuration
    @RequiredArgsConstructor
    public static class SchedulerConfiguration implements SchedulingConfigurer {

        @Override
        public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
            UserDetails userDetails = new User("_scheduledTask", "", new ArrayList<GrantedAuthority>());
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContext taskSecurityContext = new SecurityContextImpl();
            taskSecurityContext.setAuthentication(authentication);
            ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
            threadPoolTaskScheduler.setPoolSize(10);
            threadPoolTaskScheduler.initialize();
            DelegatingSecurityContextScheduledExecutorService executorService =  new DelegatingSecurityContextScheduledExecutorService(threadPoolTaskScheduler.getScheduledExecutor(), taskSecurityContext);
            taskRegistrar.setScheduler(executorService);
        }

    }

}
