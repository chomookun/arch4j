package org.chomookun.arch4j.core.code;

import org.springframework.data.redis.listener.ChannelTopic;

public class CodeChannels {

    public static final String CODE_EVICT = "core:code:evict";

    public static final ChannelTopic CODE_EVICT_CHANNEL = ChannelTopic.of(CODE_EVICT);

}
