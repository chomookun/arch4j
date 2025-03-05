package org.chomookun.arch4j.daemon;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
@RequiredArgsConstructor
public class DaemonPropertiesHolder {

    private final DaemonProperties daemonProperties;

    @Getter
    private static DaemonProperties instance;

    @PostConstruct
    public void init() {
        instance = daemonProperties;
    }

}
