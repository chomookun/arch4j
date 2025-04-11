package org.chomookun.arch4j.core.storage.client.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.chomookun.arch4j.core.storage.client.StorageClient;
import org.chomookun.arch4j.core.storage.model.StorageResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FileStorageClient extends StorageClient {

    private final String location;

    public FileStorageClient(Properties properties) {
        super(properties);
        this.location = properties.getProperty("location");
    }

    @Override
    public List<StorageResource> getChildResources(String resourceId) {
        List<StorageResource> resources = new ArrayList<>();
        File file = new File(location, resourceId);
        if (!file.exists()) {
            throw new RuntimeException(String.format("resourceId[%s] is not exists", resourceId));
        }
        File[] childFiles = file.listFiles();
        if (childFiles != null) {
            for (File childFile : childFiles) {
                StorageResource resource = new FileStorageResource(location, childFile);
                resources.add(resource);
            }
        }
        return resources;
    }

    @Override
    public StorageResource getParentResource(String resourceId) {
        File file = new File(location, resourceId).getAbsoluteFile();
        File parentFile = file.getParentFile();
        if (parentFile != null) {
            StorageResource parentResource = new FileStorageResource(location, parentFile);
            // parent resource 가 location 이거나 그 상위 디렉터리인 경우 null 반환
            File locationRoot = new File(location).getAbsoluteFile();
            if (!parentFile.toPath().startsWith(locationRoot.toPath())) {
                return null;
            }
            return parentResource;
        }
        return null;
    }

    @Override
    public StorageResource getResource(String resourceId) {
        File file = new File(location, resourceId);
        return new FileStorageResource(location, file);
    }

    @Override
    public StorageResource createFolderResource(String parentResourceId, String name) {
        File parentDirectory = new File(location, parentResourceId);
        if (!parentDirectory.exists()) {
            throw new RuntimeException(String.format("parentResourceId[%s] is not exists", parentResourceId));
        }
        String directoryName = FilenameUtils.normalizeNoEndSeparator(name);
        File directory = new File(parentDirectory, directoryName);
        if (!directory.mkdir()) {
            throw new RuntimeException("directory create error");
        }
        return new FileStorageResource(location, directory);
    }

    @Override
    public StorageResource createFileResource(String parentResourceId, String name, InputStream inputStream) {
        File directory = new File(location, parentResourceId);
        if (!directory.exists()) {
            throw new RuntimeException(String.format("parentResourceId[%s] is not exists", parentResourceId));
        }
        String filename = FilenameUtils.normalizeNoEndSeparator(name);
        File file = new File(directory, filename);
        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // disabled executable permission
        if (!file.setExecutable(false)) {
            throw new RuntimeException("file permission error");
        }
        // return resource
        return new FileStorageResource(location, file);
    }

    @Override
    public void deleteFolderResource(String resourceId) {
        File folder = new File(location, resourceId);
        // checks file exists
        if (!folder.exists()) {
            throw new RuntimeException(String.format("folder[%s] is not exists", resourceId));
        }
        if (!folder.isDirectory()) {
            throw new RuntimeException(String.format("resourceId[%s] is not directory", resourceId));
        }
        File[] childFiles = folder.listFiles();
        if (childFiles != null && childFiles.length > 0) {
            throw new RuntimeException("directory is not empty");
        }
        // delete folder
        if (!folder.delete()) {
            throw new RuntimeException("directory delete error");
        }
    }

    @Override
    public void deleteFileResource(String resourceId) {
        File file = new File(location, resourceId);
        // checks file exists
        if (!file.exists()) {
            throw new RuntimeException(String.format("resourceId[%s] is not exists", resourceId));
        }
        // checks file is directory
        if (file.isDirectory()) {
            throw new RuntimeException(String.format("resourceId[%s] is directory", resourceId));
        }
        // delete file
        if (!file.delete()) {
            throw new RuntimeException("directory delete error");
        }
    }

}
