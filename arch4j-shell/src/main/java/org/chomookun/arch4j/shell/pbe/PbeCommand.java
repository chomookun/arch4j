package org.chomookun.arch4j.shell.pbe;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.shell.common.InteractiveUtil;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class PbeCommand {

    private StandardPBEStringEncryptor createPbeEncryptor(String password) {
        StandardPBEStringEncryptor pbeEncryptor = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig pbeConfig = new EnvironmentStringPBEConfig();
        pbeConfig.setPassword(password);
        pbeEncryptor.setConfig(pbeConfig);
        return pbeEncryptor;
    }

    @ShellMethod(key = "pbe encrypt", value = "Encrypts the given text.")
    public void encrypt(String plainValue) {
        // asks password
        String password = InteractiveUtil.askInput("Enter the password");

        // encrypt
        StandardPBEStringEncryptor stringEncryptor = createPbeEncryptor(password);
        String encryptedValue = stringEncryptor.encrypt(plainValue);
        System.out.println("plain value:" + plainValue);
        System.out.println("encrypted value:" + encryptedValue);
    }

    @ShellMethod(key = "pbe decrypt", value = "Decrypts the given text.")
    public void decrypt(String encryptedValue) {
        // asks password
        String password = InteractiveUtil.askInput("Enter the password");

        // decrypt
        StandardPBEStringEncryptor stringEncryptor = createPbeEncryptor(password);
        String plainValue = stringEncryptor.decrypt(encryptedValue);
        System.out.println("encrypted value:" + encryptedValue);
        System.out.println("plain value:" + plainValue);
    }

}
