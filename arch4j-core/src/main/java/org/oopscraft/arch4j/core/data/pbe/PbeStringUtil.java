package org.oopscraft.arch4j.core.data.pbe;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

@Component
@Lazy(false)
public class PbeStringUtil implements ApplicationContextAware {

    private static StandardPBEStringEncryptor pbeStringEncryptor;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        pbeStringEncryptor = applicationContext.getBean(StandardPBEStringEncryptor.class);
    }

    public static String encrypt(String value) {
        return pbeStringEncryptor.encrypt(value);
    }

    public static String decrypt(String value) {
        return pbeStringEncryptor.decrypt(value);
    }

    public static boolean hasEncryptedMark(String value ) {
        if (value == null) {
            return false;
        }
        value = value.trim();
        return (value.startsWith("ENC(") && value.endsWith(")"));
    }

    public static boolean hasDecryptedMark(String value) {
        if (value == null) {
            return false;
        }
        value = value.trim();
        return (value.startsWith("DEC(") && value.endsWith(")"));
    }

    public static String wrapEncryptedMark(String value) {
        return "ENC(" + value + ")";
    }

    public static String wrapDecryptedMark(String value) {
        return "DEC(" + value + ")";
    }

    public static String unwrapEncryptedMark(String value) {
        return value.substring(
                "ENC(".length(),
                (value.length() - ")".length()));
    }

    public static String unwrapDecryptedMark(String value) {
        return value.substring(
                "DEC(".length(),
                (value.length() - ")".length()));
    }

    public static String encode(String value) {
        if(hasDecryptedMark(value)) {
            value = unwrapDecryptedMark(value);
            value = encrypt(value);
            return wrapEncryptedMark(value);
        }
        return value;
    }

    public static String decode(String value) {
        if(hasEncryptedMark(value)) {
            value = unwrapEncryptedMark(value);
            value = decrypt(value);
            return wrapDecryptedMark(value);
        }
        return value;
    }








//
//
//    private static boolean hasEncryptedBracket(String value) {
//        if (value == null) {
//            return false;
//        }
//        value = value.trim();
//        return (value.startsWith("ENC(") && value.endsWith(")"));
//    }
//
//    private static String getEncryptedValue(String value) {
//        return value.substring(
//                "ENC(".length(),
//                (value.length() - ")".length()));
//    }
//
//    private static boolean hasDecryptedBracket(String value) {
//        if (value == null) {
//            return false;
//        }
//        value = value.trim();
//        return (value.startsWith("DEC(") && value.endsWith(")"));
//    }
//
//    private static String getDecryptedValue(String value) {
//        return value.substring(
//                "ENC(".length(),
//                (value.length() - ")".length()));
//    }
//
//    public static String encrypt(String value) {
//        if(hasDecryptedBracket(value)) {
//            return pbeStringEncryptor.encrypt(getDecryptedValue(value));
//        }else{
//            return value;
//        }
//    }
//
//    public static String encryptWithBracket(String value) {
//        if(hasDecryptedBracket(value)) {
//            return "ENC(" + encrypt(value) + ")"
//        }else{
//            return value;
//        }
//    }
//
//    public static String decrypt(String value) {
//        if(hasEncryptedBracket(value)) {
//            return pbeStringEncryptor.decrypt(getEncryptedValue(value));
//        }else{
//            return value;
//        }
//    }
//
//    public static String decryptWithBracket(String value) {
//        if(hasEncryptedBracket(value)) {
//            return "DEC(" + decrypt(value) + ")";
//        }
//    }



//    public static String decrypt(String value) {
//        Assert.notNull(pbeStringEncryptor, "pbeStringEncryptor is null");
//        if(hasEncryptedBracket(value)) {
//            return pbeStringEncryptor.decrypt(getEncryptedValue(value));
//        }
//        return value;
//    }
//
//    public static String encrypt(String value) {
//        Assert.notNull(pbeStringEncryptor, "pbeStringEncryptor is null");
//
//
//        if(isEncrypted(value)) {
//            return ENCRYPTED_VALUE_PREFIX
//                    + pbeStringEncryptor.encrypt(getInnerEncryptedValue(value))
//                    + ENCRYPTED_VALUE_SUFFIX;
//        }else{
//            return value;
//        }
//    }

}
