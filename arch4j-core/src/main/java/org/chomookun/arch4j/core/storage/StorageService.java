package org.chomookun.arch4j.core.storage;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.ValidationUtil;
import org.chomookun.arch4j.core.storage.entity.StorageEntity;
import org.chomookun.arch4j.core.storage.model.*;
import org.chomookun.arch4j.core.storage.repository.StorageRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        storageEntity.setClientType(storage.getClientType());
        storageEntity.setClientProperties(storage.getClientProperties());
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

}
