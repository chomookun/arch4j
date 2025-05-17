package org.chomookun.arch4j.core.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.security.model.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Lazy(false)
@RequiredArgsConstructor
@Slf4j
public class CachedRoleService implements MessageListener {

    private final Cache<String, List<Role>> cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10000)
            .build();

    private final RedisMessageListenerContainer container;

    private final RoleService roleService;

    /**
     * Initializes
     */
    @PostConstruct
    private void initialize() {
        container.addMessageListener(this, SecurityChannels.ROLE_EVICT_CHANNEL);
        container.addMessageListener(this, SecurityChannels.AUTHORITY_EVICT_CHANNEL);
    }

    /**
     * Gets roles
     * @return roles
     */
    public List<Role> getRoles() {
        return cache.get("*", key -> roleService.getRoles());
    }

    /**
     * Gets role
     * @param roleId role id
     * @return role
     */
    public Optional<Role> getRole(String roleId) {
        return getRoles().stream()
                .filter(role -> role.getRoleId().equals(roleId))
                .findFirst();
    }

    /**
     * On message
     * @param message message
     * @param pattern pattern
     */
    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        log.info("Evicting cached roles - {}", message);
        cache.invalidate("*");
    }

}
