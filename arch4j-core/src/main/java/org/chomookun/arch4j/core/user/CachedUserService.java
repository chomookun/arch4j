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
import org.springframework.data.redis.listener.ChannelTopic;
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

    @PostConstruct
    public void init() {
        container.addMessageListener(this, ChannelTopic.of(UserChannels.USER_EVICT));
        container.addMessageListener(this, ChannelTopic.of(SecurityChannels.ROLE_EVICT));
        container.addMessageListener(this, ChannelTopic.of(SecurityChannels.AUTHORITY_EVICT));
    }

    public Optional<User> getUser(String userId) {
        return Optional.ofNullable(cache.get(userId, key ->
                userService.getUser(key).orElse(null)));
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
        switch (channel) {
            case UserChannels.USER_EVICT -> invalidate(messageBody);
            case SecurityChannels.ROLE_EVICT -> invalidateByRoleId(messageBody);
            case SecurityChannels.AUTHORITY_EVICT -> invalidateByAuthorityId(messageBody);
            default -> log.warn("Unknown channel: {}", channel);
        }
    }

    public void invalidate(String userId) {
        cache.invalidate(userId);
    }

    public void invalidateByRoleId(String roleId) {
        cache.asMap().forEach((userId, user) -> {
            if (user.getRoles().stream().anyMatch(role -> role.getRoleId().equals(roleId))) {
                cache.invalidate(userId);
            }
        });
    }

    public void invalidateByAuthorityId(String authorityId) {
        cache.asMap().forEach((userId, user) -> {
            if (user.getAuthorities().stream().anyMatch(authority -> authority.getAuthorityId().equals(authorityId))) {
                cache.invalidate(userId);
            }
        });
    }

}
