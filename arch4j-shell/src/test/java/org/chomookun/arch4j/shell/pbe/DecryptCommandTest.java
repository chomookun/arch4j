package org.chomookun.arch4j.shell.pbe;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.shell.common.test.ShellTestSupport;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class DecryptCommandTest extends ShellTestSupport {

    private final DecryptCommand decryptCommand;

    @Test
    void decrypt() {
        // given
        String encryptedValue = "lrZwmK3/KnE2tBbfTgEIY/AVjTNCb9je";
        String secretKey = "password";
        // then
        setIn(secretKey);
        decryptCommand.decrypt(encryptedValue);
    }

}