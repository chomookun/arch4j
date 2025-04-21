package org.chomookun.arch4j.core.storage;

import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageObjectService {

    private final StorageObjectRepository storageObjectRepository;

    private final StorageResourceService storageResourceService;

    public StorageObject uploadStorageObject(String refType, String refId, MultipartFile multipartFile, String storageId) throws IOException {
        // creates resource
        String parentResourceId = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now());
        String encodedFilename = IdGenerator.uuid();
        String filename = multipartFile.getOriginalFilename();
        Long size = multipartFile.getSize();
        InputStream inputStream = multipartFile.getInputStream();
        StorageResource storageResource = storageResourceService.createStorageFile(storageId, parentResourceId, encodedFilename, inputStream);
        // saves object info
        StorageObjectEntity storageObjectEntity = StorageObjectEntity.builder()
                .objectId(IdGenerator.uuid())
                .refType(refType)
                .refId(refId)
                .filename(filename)
                .size(size)
                .storageId(storageId)
                .resourceId(storageResource.getResourceId())
                .build();
        StorageObjectEntity savedStorageObjectEntity = storageObjectRepository.saveAndFlush(storageObjectEntity);
        return StorageObject.from(savedStorageObjectEntity);
    }

    public void downloadStorageObject(String objectId, HttpServletResponse response) {
        StorageObjectEntity storageObjectEntity = storageObjectRepository.findById(objectId).orElseThrow();
        String storageId = storageObjectEntity.getStorageId();
        String resourceId = storageObjectEntity.getResourceId();
        StorageResource storageResource = storageResourceService.getStorageResource(storageId, resourceId);
        // gets resource input stream
        try (InputStream inputStream = storageResource.getInputStream()) {
            // sets response headers
            response.setContentType("application/octet-stream");
            String filename = storageObjectEntity.getFilename();
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition",String.format("attachment; filename=\"%s\";", encodedFilename));
            // writes stream
            StreamUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteStorageObject(String objectId) {
        StorageObjectEntity storageObjectEntity = storageObjectRepository.findById(objectId).orElseThrow();
        storageResourceService.deleteStorageFile(storageObjectEntity.getStorageId(), storageObjectEntity.getResourceId());
        storageObjectRepository.delete(storageObjectEntity);
        storageObjectRepository.flush();
    }

    public Page<StorageObject> getStorageObjects(StorageObjectSearch storageObjectSearch, Pageable pageable) {
        Page<StorageObjectEntity> storageObjectEntityPage = storageObjectRepository.findAll(storageObjectSearch, pageable);
        List<StorageObject> storageObjects = storageObjectEntityPage.getContent().stream()
                .map(StorageObject::from)
                .toList();
        long total = storageObjectEntityPage.getTotalElements();
        return new PageImpl<>(storageObjects, pageable, total);
    }

    public List<StorageObject> getStorageObjects(String refType, String refId) {
        return storageObjectRepository.findAllByRef(refType, refId).stream()
                .map(StorageObject::from)
                .collect(Collectors.toList());
    }

    public StorageObject getStorageObject(String objectId) {
        return storageObjectRepository.findById(objectId)
                .map(StorageObject::from)
                .orElseThrow();
    }

}
