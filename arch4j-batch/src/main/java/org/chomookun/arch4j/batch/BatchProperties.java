package org.chomookun.arch4j.batch;

import com.zaxxer.hikari.HikariConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;

import java.io.File;

@ConfigurationProperties(prefix = "batch")
@Lazy(false)
@Validated
@AllArgsConstructor
@Getter
public final class BatchProperties {

    private String dataHome;

}
