package org.oopscraft.arch4j.core.common.pbe;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

public class PbePropertiesUtil {

    private static Properties processProperties(Properties properties, Function<String, String> processFunction) {
        Properties newProperties = new Properties();
        properties.forEach((keyObject, valueObject) -> {
            String key = (String) keyObject;
            String value = Optional.ofNullable(valueObject)
                    .map(Object::toString)
                    .orElse(null);
            value = processFunction.apply(value);
            newProperties.setProperty(key, value);
        });
        return newProperties;
    }

    private static String processProperties(String properties, Function<String,String> processFunction) {
        StringBuilder stringBuilder = new StringBuilder();
        try (StringReader stringReader = new StringReader(properties)) {
            List<String> lines = IOUtils.readLines(stringReader);
            for(String line : lines) {
                if(line.isBlank() || line.startsWith("#")) {
                    stringBuilder.append(line)
                            .append('\n');
                    continue;
                }
                int indexOf = line.indexOf("=");
                String key = line.substring(0, indexOf);
                String value = line.substring(indexOf+1);
                value = processFunction.apply(value);
                stringBuilder.append(key)
                        .append("=")
                        .append(value)
                        .append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

    public static Properties encode(Properties properties) {
        return processProperties(properties, PbeStringUtil::encode);
    }

    public static String encode(String properties) {
        return processProperties(properties, PbeStringUtil::encode);
    }

    public static Properties decode(Properties properties) {
        return processProperties(properties, PbeStringUtil::decode);
    }

    public static String decode(String properties) {
        return processProperties(properties, PbeStringUtil::decode);
    }

    public static Properties unwrapEncryptedMark(Properties properties) {
        return processProperties(properties, PbeStringUtil::unwrapEncryptedMark);
    }

    public static String unwrapEncryptedMark(String properties) {
        return processProperties(properties, PbeStringUtil::unwrapEncryptedMark);
    }

    public static Properties unwrapDecryptedMark(Properties properties) {
        return processProperties(properties, PbeStringUtil::unwrapDecryptedMark);
    }

    public static String unwrapDecryptedMark(String properties) {
        return processProperties(properties, PbeStringUtil::unwrapDecryptedMark);
    }

    public static String propertiesToString(Properties properties) {
        StringBuilder stringBuilder = new StringBuilder();
        properties.forEach((key, value) -> {
            stringBuilder.append(key)
                    .append("=")
                    .append(value)
                    .append('\n');
        });
        return stringBuilder.toString();
    }

    public static Properties stringToProperties(String propertiesString) {
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(propertiesString));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

}
