package org.chomookun.arch4j.core.common.test;

import com.github.javaparser.utils.LineSeparator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
public class CoreTestUtil {

    /**
     * Reads test resource as stream
     * @param pkg package
     * @param fileName file name
     * @return file content as stream
     */
    public static InputStream readTestResourceAsStream(Package pkg, @NotNull String fileName) {
        String packageDir = pkg.getName().replace(".", "/") + "/";
        String filePath = packageDir + fileName;
        InputStream inputStream = CoreTestUtil.class.getClassLoader().getResourceAsStream(filePath);
        Objects.requireNonNull(inputStream, "Resource not found: " + filePath);
        return inputStream;
    }

    /**
     * Reads test resource as string
     * @param pkg package
     * @param fileName file name
     * @return file content as string
     */
    public static String readTestResourceAsString(Package pkg, @NotNull String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = readTestResourceAsStream(pkg, fileName)) {
            IOUtils.readLines(inputStream, StandardCharsets.UTF_8).forEach(line -> {
                stringBuilder.append(line).append(LineSeparator.LF);
            });
            return stringBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads file as string
     * @param filePath file path
     * @return file content as string
     */
    public static String readFileAsString(String filePath) {
        log.info("Working Directory: {}", System.getProperty("user.dir"));
        try {
            Path path = Paths.get(filePath);
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
