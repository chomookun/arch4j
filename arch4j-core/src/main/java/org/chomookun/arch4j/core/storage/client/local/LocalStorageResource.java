package org.chomookun.arch4j.core.storage.client.local;

import org.chomookun.arch4j.core.storage.model.StorageResource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;

public class LocalStorageResource extends FileSystemResource implements StorageResource {

    private final String location;

    public LocalStorageResource(String location, File file) {
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
    public String getFilename() {
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
    public long getSize() {
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
