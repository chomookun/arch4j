package org.chomookun.arch4j.core.storage;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.storage.client.StorageClient;
import org.chomookun.arch4j.core.storage.client.StorageClientFactory;
import org.chomookun.arch4j.core.storage.model.Storage;
import org.chomookun.arch4j.core.storage.model.StorageResource;
import org.chomookun.arch4j.core.storage.model.StorageResourceInfo;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageResourceService {

    private final StorageService storageService;

    public StorageResourceInfo getStorageResourceInfo(String storageId, String resourceId) {
        Storage storage = storageService.getStorage(storageId).orElseThrow();
        StorageClient storageClient = StorageClientFactory.getStorageClient(storage);
        // if root folder request, initializes root folder
        if (resourceId == null || resourceId.trim().isEmpty()) {
            storageClient.initializeRootFolder();
        }
        // resource
        StorageResource storageResource = storageClient.getResource(resourceId);
        StorageResourceInfo storageResourceInfo = StorageResourceInfo.from(storageResource);
        storageResourceInfo.setStorageId(storageId);
        // parent resource
        StorageResource parentStorageResource = storageClient.getParentResource(resourceId);
        StorageResourceInfo parentStorageResourceInfo = Optional.ofNullable(parentStorageResource)
                .map(StorageResourceInfo::from)
                .orElse(null);
        storageResourceInfo.setParentStorageResource(parentStorageResourceInfo);
        // child resources
        List<StorageResource> childStorageResources = storageClient.getChildResources(resourceId);
        childStorageResources.sort((o1, o2) -> {
            if (o1.getType() == StorageResource.Type.FOLDER && o2.getType() != StorageResource.Type.FOLDER) {
                return -1;
            } else if (o1.getType() != StorageResource.Type.FOLDER && o2.getType() == StorageResource.Type.FOLDER) {
                return 1;
            } else {
                return 0;
            }
        });
        List<StorageResourceInfo> childStorageResourceInfos = childStorageResources.stream()
                .map(StorageResourceInfo::from)
                .toList();
        storageResourceInfo.setChildStorageResources(childStorageResourceInfos);
        // returns
        return storageResourceInfo;
    }

    public StorageResource getStorageResource(String storageId, String resourceId) {
        Storage storage = storageService.getStorage(storageId).orElseThrow();
        StorageClient storageClient = StorageClientFactory.getStorageClient(storage);
        return storageClient.getResource(resourceId);
    }

    public StorageResource createStorageFolder(String storageId, String parentResourceId, String name) {
        Storage storage = storageService.getStorage(storageId).orElseThrow();
        StorageClient storageClient = StorageClientFactory.getStorageClient(storage);
        return storageClient.createFolderResource(parentResourceId, name);
    }

    public StorageResource createStorageFile(String storageId, String parentResourceId, String name, InputStream inputStream) {
        Storage storage = storageService.getStorage(storageId).orElseThrow();
        StorageClient storageClient = StorageClientFactory.getStorageClient(storage);
        return storageClient.createFileResource(parentResourceId, name, inputStream);
    }

    public void deleteStorageFolder(String storageId, String resourceId) {
        Storage storage = storageService.getStorage(storageId).orElseThrow();
        StorageClient storageClient = StorageClientFactory.getStorageClient(storage);
        storageClient.deleteFolderResource(resourceId);
    }

    public void deleteStorageFile(String storageId, String resourceId) {
        Storage storage = storageService.getStorage(storageId).orElseThrow();
        StorageClient storageClient = StorageClientFactory.getStorageClient(storage);
        storageClient.deleteFileResource(resourceId);
    }

}
