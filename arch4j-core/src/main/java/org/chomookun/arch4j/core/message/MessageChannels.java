package org.chomookun.arch4j.core.message;

import org.springframework.data.redis.listener.ChannelTopic;

public class MessageChannels {

    public static final String MESSAGE_EVICT = "core:message:evict";

    public static final ChannelTopic MESSAGE_EVICT_CHANNEL = ChannelTopic.of(MESSAGE_EVICT);

}
