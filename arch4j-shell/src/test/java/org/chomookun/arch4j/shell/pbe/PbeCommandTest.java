package org.chomookun.arch4j.shell.pbe;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.shell.common.test.ShellTestSupport;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class PbeCommandTest extends ShellTestSupport {

    private final PbeCommand pbeCommand;

    @Test
    void encrypt() {
        // given
        String plainValue = "test_value";
        String secretKey = "password";
        // then
        setIn(secretKey);
        pbeCommand.encrypt(plainValue);
    }

    @Test
    void decrypt() {
        // given
        String encryptedValue = "lrZwmK3/KnE2tBbfTgEIY/AVjTNCb9je";
        String secretKey = "password";
        // then
        setIn(secretKey);
        pbeCommand.decrypt(encryptedValue);
    }
}