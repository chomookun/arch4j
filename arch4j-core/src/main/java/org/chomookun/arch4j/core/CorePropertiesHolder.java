package org.chomookun.arch4j.core;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
@RequiredArgsConstructor
public class CorePropertiesHolder {

    private final CoreProperties coreProperties;

    @Getter
    private static CoreProperties instance;

    @PostConstruct
    public void init() {
        instance = coreProperties;
    }

}
