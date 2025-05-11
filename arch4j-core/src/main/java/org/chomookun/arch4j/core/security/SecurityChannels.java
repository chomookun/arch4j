package org.chomookun.arch4j.core.security;

import org.springframework.data.redis.listener.ChannelTopic;

public class SecurityChannels {

    public static final String ROLE_EVICT = "core:security:role:evict";

    public static final String AUTHORITY_EVICT = "core:security:authority:evict";

}
