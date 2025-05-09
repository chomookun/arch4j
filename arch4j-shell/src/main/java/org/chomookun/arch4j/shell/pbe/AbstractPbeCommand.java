package org.chomookun.arch4j.shell.pbe;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.shell.common.InteractiveUtil;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

public class AbstractPbeCommand {

    protected StandardPBEStringEncryptor createPbeEncryptor(String password) {
        StandardPBEStringEncryptor pbeEncryptor = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig pbeConfig = new EnvironmentStringPBEConfig();
        pbeConfig.setPassword(password);
        pbeEncryptor.setConfig(pbeConfig);
        return pbeEncryptor;
    }

}
