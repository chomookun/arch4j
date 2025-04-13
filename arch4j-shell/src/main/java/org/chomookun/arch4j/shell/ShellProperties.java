package org.chomookun.arch4j.shell;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;

@ConfigurationProperties(prefix = "shell")
@Lazy(false)
@AllArgsConstructor
@Getter
public final class ShellProperties {

}
