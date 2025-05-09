package org.chomookun.arch4j.shell.pbe;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.shell.common.InteractiveUtil;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@ShellCommandGroup("Pbe Commands")
@RequiredArgsConstructor
public class EncryptCommand extends AbstractPbeCommand {

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

}
