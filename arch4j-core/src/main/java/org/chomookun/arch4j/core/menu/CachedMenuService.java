package org.chomookun.arch4j.core.menu;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.menu.model.Menu;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
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
public class CachedMenuService implements MessageListener {

    private final Cache<String, List<Menu>> cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10000)
            .build();

    private final RedisMessageListenerContainer container;

    private final MenuService menuService;

    @PostConstruct
    public void initialize() {
        container.addMessageListener(this, MenuChannels.MENU_EVICT_CHANNEL);
    }

    public List<Menu> getMenus() {
        return cache.get("*", key -> {
            return menuService.getMenus();
        });
    }

    public Optional<Menu> getMenu(String menuId) {
        return getMenus().stream()
                .filter(menu -> menu.getMenuId().equals(menuId))
                .findFirst();
    }

    @Override
    public void onMessage(@NotNull org.springframework.data.redis.connection.Message message, byte[] pattern) {
        log.info("Evicting cached menu - {}", message);
        cache.invalidate("*");
    }

}
