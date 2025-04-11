package org.chomookun.arch4j.core.storage.client.s3;

import org.chomookun.arch4j.core.storage.model.StorageResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.time.Instant;

/**
 * todo
 */
public class S3StorageResource implements StorageResource {

    @Override
    public String getResourceId() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public long getLength() {
        return 0;
    }

    @Override
    public Instant getLastModified() {
        return null;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public URL getURL() throws IOException {
        return null;
    }

    @Override
    public URI getURI() throws IOException {
        return null;
    }

    @Override
    public File getFile() throws IOException {
        return null;
    }

    @Override
    public long contentLength() throws IOException {
        return 0;
    }

    @Override
    public long lastModified() throws IOException {
        return 0;
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        return null;
    }

    @Override
    public String getFilename() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }
}