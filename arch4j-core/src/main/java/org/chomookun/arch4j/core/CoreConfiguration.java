package org.chomookun.arch4j.core;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.h2.tools.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.chomookun.arch4j.core.message.MessageService;
import org.chomookun.arch4j.core.message.MessageSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.*;
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
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
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
     * DataSource hikari config
     * @return hikari config
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    /**
     * Creates DataSource
     * @param hikariConfig hikari config
     * @return dataSource
     */
    @Bean
    @Primary
    public DataSource dataSource(HikariConfig hikariConfig, ConfigurableEnvironment environment) {
        // if embedded, replace to cluster
        if (hikariConfig.getJdbcUrl().contains(":h2:mem")) {
            String jdbcUrlCluster = environment.getProperty("spring.datasource.hikari.jdbc-url-cluster");
            if (jdbcUrlCluster != null) {
                try (Connection connection = DriverManager.getConnection(jdbcUrlCluster, hikariConfig.getUsername(), hikariConfig.getPassword())) {
                    connection.isValid(1);
                    hikariConfig.setJdbcUrl(jdbcUrlCluster);
                } catch (SQLException ignore) {}
            }
        }
        // Creates hikari dataSource
        return new HikariDataSource(hikariConfig);
    }

    /**
     * In-memory H2 database server
     * @param dataSource dataSource
     * @return h2 server if h2 embedded
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server inMemoryH2DatabaseServer(DataSource dataSource) throws SQLException {
        if (EmbeddedDatabaseConnection.isEmbedded(dataSource)) {
            return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
        }
        return null;
    }

    /**
     * Batch dataSource hikari config
     * @return hikari config
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.batch.datasource.hikari")
    public HikariConfig batchHikariConfig() {
        return new HikariConfig();
    }

    /**
     * Batch dataSource
     * @param batchHikariConfig batch hiakri config
     * @param environment environment
     * @return batch dataSource
     */
    @Bean
    @BatchDataSource
    public DataSource batchDataSource(HikariConfig batchHikariConfig, ConfigurableEnvironment environment) {
        // if embedded, replace to cluster
        if (batchHikariConfig.getJdbcUrl().contains(":h2:mem")) {
            String jdbcUrlCluster = environment.getProperty("spring.batch.datasource.hikari.jdbc-url-cluster");
            if (jdbcUrlCluster != null) {
                try (Connection connection = DriverManager.getConnection(jdbcUrlCluster, batchHikariConfig.getUsername(), batchHikariConfig.getPassword())) {
                    connection.isValid(1);
                    batchHikariConfig.setJdbcUrl(jdbcUrlCluster);
                } catch (SQLException ignore) {}
            }
        }
        // Creates batch hikari dataSource
        return new HikariDataSource(batchHikariConfig);
    }

    /**
     * Batch in-memory HSQLDB database server
     * @param batchDataSource batch dataSource
     * @return hsqldb server if hsqldb embedded
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public org.hsqldb.server.Server batchInMemoryHsqldbDatabaseServer(@Qualifier("batchDataSource")DataSource batchDataSource) throws SQLException {
        if (EmbeddedDatabaseConnection.isEmbedded(batchDataSource)) {
            HsqlProperties properties = new HsqlProperties();
            properties.setProperty("server.database.0", "mem:testdb");
            properties.setProperty("server.dbname.0", "test");
            properties.setProperty("server.port", "9001");
            properties.setProperty("server.remote_open", "true");
            org.hsqldb.server.Server server = new org.hsqldb.server.Server();
            try {
                server.setProperties(properties);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            server.setLogWriter(null);
            server.setErrWriter(null);
            return server;
        }
        return null;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RedisServer redisServer(ConfigurableEnvironment environment) throws IOException {
        String host = environment.getProperty("spring.data.redis.host");
        if ("localhost".equals(host) || "127.0.0.1".equals(host)) {
            int port = Integer.parseInt(Objects.requireNonNull(environment.getProperty("spring.data.redis.port")));
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
