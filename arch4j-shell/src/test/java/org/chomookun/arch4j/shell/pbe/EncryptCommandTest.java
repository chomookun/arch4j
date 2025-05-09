package org.chomookun.arch4j.shell.pbe;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.shell.common.test.ShellTestSupport;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class EncryptCommandTest extends ShellTestSupport {

    private final EncryptCommand encryptCommand;

    @Test
    void encrypt() {
        // given
        String plainValue = "test_value";
        String secretKey = "password";
        // then
        setIn(secretKey);
        encryptCommand.encrypt(plainValue);
    }

}