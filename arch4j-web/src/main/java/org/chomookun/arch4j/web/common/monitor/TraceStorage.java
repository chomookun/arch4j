package org.chomookun.arch4j.web.common.monitor;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TraceStorage {

    private final Cache<String, Trace> cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10000)
            .build();

    public void storeTrace(Trace trace) {

    }

}
