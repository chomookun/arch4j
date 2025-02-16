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

    @Getter
    private static DaemonProperties instance;

    private final DaemonProperties daemonProperties;

    @PostConstruct
    public void postConstruct() {
        instance = daemonProperties;
    }

}
