package org.chomookun.arch4j.core.user;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.security.SecurityChannels;
import org.chomookun.arch4j.core.user.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Lazy(false)
@RequiredArgsConstructor
@Slf4j
public class CachedUserService implements MessageListener {

    private final Cache<String, User> cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10000)
            .build();

    private final RedisMessageListenerContainer container;

    private final UserService userService;

    /**
     * Initializes
     */
    @PostConstruct
    private void initialize() {
        container.addMessageListener(this, UserChannels.USER_EVICT_CHANNEL);
        container.addMessageListener(this, SecurityChannels.ROLE_EVICT_CHANNEL);
        container.addMessageListener(this, SecurityChannels.AUTHORITY_EVICT_CHANNEL);
    }

    /**
     * Gets user by userId
     * @param userId user id
     * @return user
     */
    public Optional<User> getUser(String userId) {
        return Optional.ofNullable(cache.get(userId, key ->
                userService.getUser(key).orElse(null)));
    }

    /**
     * Listens messages from Redis channels
     * @param message message
     * @param pattern pattern
     */
    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
        switch (channel) {
            case UserChannels.USER_EVICT -> invalidateUser(messageBody);
            case SecurityChannels.ROLE_EVICT -> invalidateUsersByRoleId(messageBody);
            case SecurityChannels.AUTHORITY_EVICT -> invalidateUsersByAuthorityId(messageBody);
            default -> log.warn("Unknown channel: {}", channel);
        }
    }

    /**
     * Invalidate user
     * @param userId changed user id
     */
    void invalidateUser(String userId) {
        cache.invalidate(userId);
    }

    /**
     * Invalidate user by role id
     * @param roleId changed role id
     */
    void invalidateUsersByRoleId(String roleId) {
        cache.asMap().forEach((userId, user) -> {
            if (user.getRoles().stream().anyMatch(role -> role.getRoleId().equals(roleId))) {
                cache.invalidate(userId);
            }
        });
    }

    /**
     * Invalidate by authority id
     * @param authorityId changed authority id
     */
    void invalidateUsersByAuthorityId(String authorityId) {
        cache.asMap().forEach((userId, user) -> {
            if (user.getAuthorities().stream().anyMatch(authority -> authority.getAuthorityId().equals(authorityId))) {
                cache.invalidate(userId);
            }
        });
    }

}
