package org.chomookun.arch4j.core.storage.client.file;

import org.chomookun.arch4j.core.storage.model.StorageResource;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;

public class FileStorageResource extends FileSystemResource implements StorageResource {

    private final String location;

    public FileStorageResource(String location, File file) {
        super(file);
        this.location = location;
    }

    @Override
    public String getResourceId() {
        Path locationPath = Path.of(location);
        Path filePath = Path.of(super.getFile().getAbsolutePath());
        Path relativePath = locationPath.relativize(filePath);
        return relativePath.toString();
    }

    @Override
    public String getName() {
        return super.getFilename();
    }

    @Override
    public Type getType() {
        File file = super.getFile();
        if (file.exists()) {
            return file.isDirectory() ? Type.FOLDER : Type.FILE;
        } else {
            return null;
        }
    }

    @Override
    public long getLength() {
        File file = super.getFile();
        if (file.exists()) {
            return file.length();
        } else {
            return 0;
        }
    }

    @Override
    public Instant getLastModified() {
        File file = super.getFile();
        if (file.exists()) {
            return Instant.ofEpochMilli(file.lastModified());
        } else {
            return null;
        }
    }
}
