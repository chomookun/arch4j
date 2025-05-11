package org.chomookun.arch4j.core.message;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.message.model.Message;
import org.chomookun.arch4j.core.security.SecurityChannels;
import org.chomookun.arch4j.core.user.UserChannels;
import org.chomookun.arch4j.core.user.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
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
public class CachedMessageService implements MessageListener {

    private final Cache<String, Message> cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10000)
            .build();

    private final RedisMessageListenerContainer container;

    private final MessageService messageService;

    @PostConstruct
    public void init() {
        container.addMessageListener(this, ChannelTopic.of(MessageChannels.MESSAGE_EVICT));
    }

    public Optional<Message> getMessage(String messageId) {
        return Optional.ofNullable(cache.get(messageId, key ->
                messageService.getMessage(key).orElse(null)));
    }

    @Override
    public void onMessage(@NotNull org.springframework.data.redis.connection.Message message, byte[] pattern) {
        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
        cache.invalidate(messageBody);
    }

}
