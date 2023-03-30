package org.oopscraft.arch4j.core.data.crypto;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Locale;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CryptoUtilTest {

    @ParameterizedTest
    @ValueSource(strings = {"en","ko","ja","zh-CN"})
    public void testDefault(String language) {
        for(int i = 0; i < 1000; i ++ ) {
            Faker faker = new Faker(new Locale(language), new Random(i));
            String plainValue = faker.name().fullName();
            log.debug("== plainValue:{}", plainValue);
            String encryptedValue = CryptoUtil.encrypt(plainValue);
            log.debug("== encryptedValue:{}", encryptedValue);
            String decryptedValue = CryptoUtil.decrypt(encryptedValue);
            log.debug("== decryptedValue:{}", decryptedValue);
            assertEquals(plainValue, decryptedValue, "decrypted value is not match plain value");
        }
    }

}