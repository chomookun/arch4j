package org.chomookun.arch4j.batch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@Lazy(false)
@RequiredArgsConstructor
public class BatchPropertiesHolder {

    private final BatchProperties batchProperties;

    @Getter
    private static BatchProperties instance;

    @PostConstruct
    public void init() {
        instance = batchProperties;
    }

}
