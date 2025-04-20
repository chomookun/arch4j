package org.chomookun.arch4j.core.storage;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.storage.entity.StorageObjectEntity;
import org.chomookun.arch4j.core.storage.model.StorageObject;
import org.chomookun.arch4j.core.storage.model.StorageObjectSearch;
import org.chomookun.arch4j.core.storage.model.StorageResource;
import org.chomookun.arch4j.core.storage.repository.StorageObjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageObjectService {

    private final StorageObjectRepository storageObjectRepository;

    private final StorageService storageService;

    private final StorageResourceService storageResourceService;

    /**
     * Creates storage object
     * @param group group
     * @param filename filename
     * @param inputStream inputStream
     * @param storageId storage id
     * @return created storage object
     */
    public StorageObject createStorageObject(String group, String filename, InputStream inputStream, String storageId) {
        // creates resource
        String parentResourceId = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now());
        String encodedFilename = IdGenerator.uuid();
        StorageResource storageResource = storageResourceService.createStorageFile(storageId, parentResourceId, encodedFilename, inputStream);
        // saves object info
        StorageObjectEntity storageObjectEntity = StorageObjectEntity.builder()
                .objectId(IdGenerator.uuid())
                .group(group)
                .storageId(storageId)
                .filename(filename)
                .storageId(storageId)
                .resourceId(storageResource.getResourceId())
                .build();
        StorageObjectEntity savedStorageObjectEntity = storageObjectRepository.saveAndFlush(storageObjectEntity);
        return StorageObject.from(savedStorageObjectEntity);
    }

    /**
     * Deletes storage object
     * @param objectId object id
     */
    public void deleteStorageObject(String objectId) {
        StorageObjectEntity storageObjectEntity = storageObjectRepository.findById(objectId).orElseThrow();
        storageResourceService.deleteStorageFile(storageObjectEntity.getStorageId(), storageObjectEntity.getResourceId());
        storageObjectRepository.delete(storageObjectEntity);
        storageObjectRepository.flush();
    }

    /**
     * Gets storage object page by search condition
     * @param storageObjectSearch storage object search condition
     * @param pageable pageable
     * @return page of storage objects
     */
    public Page<StorageObject> getStorageObjects(StorageObjectSearch storageObjectSearch, Pageable pageable) {
        Page<StorageObjectEntity> storageObjectEntityPage = storageObjectRepository.findAll(storageObjectSearch, pageable);
        List<StorageObject> storageObjects = storageObjectEntityPage.getContent().stream()
                .map(StorageObject::from)
                .toList();
        long total = storageObjectEntityPage.getTotalElements();
        return new PageImpl<>(storageObjects, pageable, total);
    }

    public List<StorageObject> getStorageObjects(String group) {
        return storageObjectRepository.findAllByGroup(group).stream()
                .map(StorageObject::from)
                .collect(Collectors.toList());
    }

    public StorageObject getStorageObject(String objectId) {
        return storageObjectRepository.findById(objectId)
                .map(StorageObject::from)
                .orElseThrow();
    }

}
