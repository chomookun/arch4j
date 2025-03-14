package org.chomookun.arch4j.core.git;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix="core.git")
@AllArgsConstructor
@Getter
@Validated
public class GitProperties {

    @NotBlank
    private final String location;

}
