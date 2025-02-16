package org.chomookun.arch4j.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.chomookun.arch4j.core.security.model.SecurityPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "web")
@AllArgsConstructor
@Getter
public final class WebProperties {

    private final String theme;

    private final String brand;

    private final String favicon;

    private final String title;

    private final String index;

}
