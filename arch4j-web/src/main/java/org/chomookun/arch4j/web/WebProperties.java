package org.chomookun.arch4j.web;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "web")
@Lazy(false)
@Validated
@AllArgsConstructor
@Getter
public final class WebProperties {

    @NotNull
    private final String theme;

    @NotNull
    private final String brand;

    @NotNull
    private final String favicon;

    @NotNull
    private final String title;

    @NotNull
    private final String index;

    private final String apiUrl;

    @NotNull
    private final Admin admin;

    @Getter
    @AllArgsConstructor
    public static final class Admin {
        private final boolean enabled;
    }

}
