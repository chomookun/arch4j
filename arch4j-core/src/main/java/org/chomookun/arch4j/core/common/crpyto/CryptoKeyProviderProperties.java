package org.chomookun.arch4j.core.common.crpyto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Properties;

@ConfigurationProperties(prefix="core.data.crypto.crypto-key-provider")
@Lazy(false)
@Validated
@AllArgsConstructor
@Getter
public class CryptoKeyProviderProperties {

    @NotBlank
    private final String bean;

    @NotNull
    private final Properties properties;

}
