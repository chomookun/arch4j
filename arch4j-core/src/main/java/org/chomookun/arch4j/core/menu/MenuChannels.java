package org.chomookun.arch4j.core.menu;

import org.springframework.data.redis.listener.ChannelTopic;

public class MenuChannels {

    public static final String MENU_EVICT = "core:menu:evict";

    public static final ChannelTopic MENU_EVICT_CHANNEL = ChannelTopic.of(MENU_EVICT);

}
