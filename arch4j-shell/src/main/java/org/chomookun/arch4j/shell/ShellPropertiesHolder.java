package org.chomookun.arch4j.shell;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
@RequiredArgsConstructor
public class ShellPropertiesHolder {

    private final ShellProperties shellProperties;

    @Getter
    private static ShellProperties instance;

    @PostConstruct
    public void init() {
        instance = shellProperties;
    }

}
