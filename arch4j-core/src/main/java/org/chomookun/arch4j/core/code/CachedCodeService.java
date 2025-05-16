package org.chomookun.arch4j.core.code;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.code.model.Code;
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
public class CachedCodeService implements MessageListener {

    private final Cache<String, Code> cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10000)
            .build();

    private final RedisMessageListenerContainer container;

    private final CodeService codeService;

    @PostConstruct
    public void initialize() {
        container.addMessageListener(this, ChannelTopic.of(CodeChannels.CODE_EVICT));
    }

    public Optional<Code> getCode(String codeId) {
        return Optional.ofNullable(cache.get(codeId, key ->
                codeService.getCode(key).orElse(null)));
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
        cache.invalidate(messageBody);
    }

}
