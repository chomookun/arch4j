package org.chomookun.arch4j.daemon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "daemon")
@AllArgsConstructor
@Getter
public final class DaemonProperties {

}
