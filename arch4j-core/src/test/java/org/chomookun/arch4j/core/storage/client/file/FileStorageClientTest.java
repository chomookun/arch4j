package org.chomookun.arch4j.core.storage.client.file;

import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.storage.model.StorageResource;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.List;
import java.util.Properties;

@Slf4j
class FileStorageClientTest {

    private static final String LOCATION = System.getProperty("user.home") + "/.arch4j/test";

    @Test
    void getChildResources() throws Throwable {
        // given
        new File(LOCATION).mkdirs();
        new File(LOCATION, "test-dir").mkdir();
        new File(LOCATION, "test1.txt").createNewFile();
        // when
        Properties properties = new Properties();
        properties.setProperty("location", LOCATION);
        FileStorageClient storageClient = new FileStorageClient(properties);
        List<StorageResource> resources = storageClient.getChildResources(".");
        log.info("resources: {}", resources);
    }

    @Test
    void upload() {
    }

    @Test
    void download() {
    }
}