package org.chomookun.arch4j.core.storage;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.ValidationUtil;
import org.chomookun.arch4j.core.storage.client.StorageClient;
import org.chomookun.arch4j.core.storage.client.StorageClientFactory;
import org.chomookun.arch4j.core.storage.entity.StorageEntity;
import org.chomookun.arch4j.core.storage.model.*;
import org.chomookun.arch4j.core.storage.repository.StorageRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageRepository storageRepository;

    @Transactional
    public Storage saveStorage(Storage storage) {
        ValidationUtil.validate(storage);
        StorageEntity storageEntity = storageRepository.findById(storage.getStorageId())
                .orElse(StorageEntity.builder()
                        .storageId(storage.getStorageId())
                        .build());
        storageEntity.setName(storage.getName());
        storageEntity.setStorageClientId(storage.getStorageClientId());
        storageEntity.setStorageClientConfig(storage.getStorageClientConfig());
        StorageEntity savedStorageEntity = storageRepository.saveAndFlush(storageEntity);
        return Storage.from(savedStorageEntity);
    }

    public Page<Storage> getStorages(StorageSearch storageSearch, Pageable pageable) {
        Page<StorageEntity> storageEntityPage = storageRepository.findAll(storageSearch, pageable);
        List<Storage> storages = storageEntityPage.getContent()
                .stream()
                .map(Storage::from)
                .toList();
        long total = storageEntityPage.getTotalElements();
        return new PageImpl<>(storages, pageable, total);
    }

    public Optional<Storage> getStorage(String storageId) {
        return storageRepository.findById(storageId)
                .map(Storage::from);
    }

    @Transactional
    public void deleteStorage(String storageId) {
        storageRepository.deleteById(storageId);
    }

    public StorageResourceSummary getStorageResourceSummary(String storageId, String resourceId) {
        Storage storage = getStorage(storageId)
                .orElseThrow();
        StorageClient storageClient = StorageClientFactory.getStorageClient(storage);
        // if root folder request, initializes root folder
        if (resourceId == null || resourceId.trim().isEmpty()) {
            storageClient.initializeRootFolder();
        }
        // parent resource
        StorageResource parentStorageResource = storageClient.getParentResource(resourceId);
        StorageResourceInfo parentStorageResourceInfo = Optional.ofNullable(parentStorageResource)
                .map(StorageResourceInfo::from)
                .orElse(null);
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
        // summary
        return StorageResourceSummary.builder()
                .storageId(storageId)
                .resourceId(resourceId)
                .parentStorageResource(parentStorageResourceInfo)
                .childStorageResources(childStorageResourceInfos)
                .build();
    }

    public StorageResource getStorageResource(String storageId, String resourceId) {
        Storage storage = getStorage(storageId).orElseThrow();
        StorageClient storageClient = StorageClientFactory.getStorageClient(storage);
        return storageClient.getResource(resourceId);
    }

    public StorageResource createStorageFolder(String storageId, String parentResourceId, String name) {
        Storage storage = getStorage(storageId).orElseThrow();
        StorageClient storageClient = StorageClientFactory.getStorageClient(storage);
        return storageClient.createFolderResource(parentResourceId, name);
    }

    public StorageResource createStorageFile(String storageId, String parentResourceId, String name, InputStream inputStream) {
        Storage storage = getStorage(storageId).orElseThrow();
        StorageClient storageClient = StorageClientFactory.getStorageClient(storage);
        return storageClient.createFileResource(parentResourceId, name, inputStream);
    }

    public void deleteStorageFolder(String storageId, String resourceId) {
        Storage storage = getStorage(storageId).orElseThrow();
        StorageClient storageClient = StorageClientFactory.getStorageClient(storage);
        storageClient.deleteFolderResource(resourceId);
    }

    public void deleteStorageFile(String storageId, String resourceId) {
        Storage storage = getStorage(storageId).orElseThrow();
        StorageClient storageClient = StorageClientFactory.getStorageClient(storage);
        storageClient.deleteFileResource(resourceId);
    }

}
