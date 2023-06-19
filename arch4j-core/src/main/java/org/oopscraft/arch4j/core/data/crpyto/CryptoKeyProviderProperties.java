package org.oopscraft.arch4j.core.data.crpyto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Properties;

@ConfigurationProperties(prefix="core.data.crypto.crypto-key-provider")
@ConstructorBinding
@AllArgsConstructor
@Getter
@Validated
public class CryptoKeyProviderProperties {

    @NotBlank
    private final String bean;

    @NotNull
    private final Properties properties;

}
