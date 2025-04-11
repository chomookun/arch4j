package org.chomookun.arch4j.core.storage.model;

import org.springframework.core.io.Resource;

import java.time.Instant;

public interface StorageResource extends Resource {

    public String getResourceId();

    public String getName();

    public Type getType();

    public long getLength();

    public Instant getLastModified();

    public enum Type {
        FILE,
        FOLDER
    }

}
