package org.chomookun.arch4j.core.user;

import org.springframework.data.redis.listener.ChannelTopic;

public class UserChannels {

    public static final String USER_EVICT = "core:user:evict";

    public static final ChannelTopic USER_EVICT_CHANNEL = ChannelTopic.of(USER_EVICT);

}
