package org.chomookun.arch4j.core.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.chomookun.arch4j.core.security.model.SecurityPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix="core.security")
@AllArgsConstructor
@Getter
@Validated
public class SecurityProperties {

    private final SecurityPolicy securityPolicy;

    private final String signingKey;

    private final Integer sessionExpireMinutes;

    private final Integer passwordExpireDays;

}
