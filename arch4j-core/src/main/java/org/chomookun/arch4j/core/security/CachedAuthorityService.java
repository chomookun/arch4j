package org.chomookun.arch4j.core.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.security.model.Authority;
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
public class CachedAuthorityService implements MessageListener {

    private final Cache<String, List<Authority>> cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10000)
            .build();

    private final RedisMessageListenerContainer container;

    private final AuthorityService authorityService;

    @PostConstruct
    public void init() {
        container.addMessageListener(this, ChannelTopic.of(SecurityChannels.AUTHORITY_EVICT));
    }

    public List<Authority> getAuthorities() {
        return cache.get("*", key -> {
            return authorityService.getAuthorities();
        });
    }

    public Optional<Authority> getAuthority(String authorityId) {
        return getAuthorities().stream()
                .filter(authority -> authority.getAuthorityId().equals(authorityId))
                .findFirst();
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        log.info("Evicting cached authorities - {}", message);
        cache.invalidate("*");
    }

}
