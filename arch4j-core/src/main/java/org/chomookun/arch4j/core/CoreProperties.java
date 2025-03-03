package org.chomookun.arch4j.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Locale;

@ConfigurationProperties(prefix = "core")
@AllArgsConstructor
@Getter
public class CoreProperties {

    private final String brand;

    private final String favicon;

    private final String title;

    private final String index;

    private final String theme;

    private final Locale defaultLocale;

    private final List<Locale> supportedLocales;

}
