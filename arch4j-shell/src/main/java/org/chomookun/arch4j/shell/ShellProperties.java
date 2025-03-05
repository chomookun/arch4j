package org.chomookun.arch4j.shell;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "shell")
@AllArgsConstructor
@Getter
public final class ShellProperties {

}
