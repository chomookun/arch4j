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
public class DecryptCommand extends AbstractPbeCommand {

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
