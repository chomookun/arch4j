package org.chomookun.arch4j.core.security;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.chomookun.arch4j.core.security.model.SecurityPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix="core.security")
@Validated
@Lazy(false)
@AllArgsConstructor
@Getter
public class SecurityProperties {

    @NotNull
    private final SecurityPolicy securityPolicy;

    @NotNull
    private final String signingKey;

    @NotNull
    private final Integer sessionExpireMinutes;

    private final Integer passwordExpireDays;

}
