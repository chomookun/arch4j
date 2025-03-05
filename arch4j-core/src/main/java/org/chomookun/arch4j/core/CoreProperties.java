package org.chomookun.arch4j.core;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Locale;

@ConfigurationProperties(prefix = "core")
@Validated
@AllArgsConstructor
@Getter
public class CoreProperties {

    @NotNull
    private final Locale defaultLocale;

    @NotNull
    private final List<Locale> supportedLocales;

}
