package org.chomookun.arch4j.core.storage.model;

import org.springframework.core.io.Resource;

import java.time.Instant;

public interface StorageResource extends Resource {

    public String getResourceId();

    public String getFilename();

    public long getSize();

    public Instant getLastModified();

    public Type getType();

    public enum Type {
        FILE,
        FOLDER
    }

}
