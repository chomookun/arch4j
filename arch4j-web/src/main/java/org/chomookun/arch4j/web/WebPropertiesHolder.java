package org.chomookun.arch4j.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@Lazy(false)
@RequiredArgsConstructor
public class WebPropertiesHolder {

    private final WebProperties webProperties;

    @Getter
    private static WebProperties instance;

    @PostConstruct
    public void init() {
        instance = webProperties;
    }

}
