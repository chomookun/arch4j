package org.chomookun.arch4j.daemon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;

@ConfigurationProperties(prefix = "daemon")
@Lazy(false)
@AllArgsConstructor
@Getter
public final class DaemonProperties {

}
