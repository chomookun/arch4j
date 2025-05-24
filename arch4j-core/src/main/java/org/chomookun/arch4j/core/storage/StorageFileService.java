package org.chomookun.arch4j.core.storage;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.storage.entity.StorageFileEntity;
import org.chomookun.arch4j.core.storage.model.StorageFile;
import org.chomookun.arch4j.core.storage.model.StorageFileSearch;
import org.chomookun.arch4j.core.storage.model.StorageResource;
import org.chomookun.arch4j.core.storage.repository.StorageFileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageFileService {

    private final StorageFileRepository storageFileRepository;

    private final StorageResourceService storageResourceService;

    @Transactional
    public StorageFile uploadStorageFile(MultipartFile multipartFile, String storageId, String targetType) throws IOException {
        // creates resource
        String parentResourceId = targetType + '/' + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now());
        String encodedFilename = IdGenerator.uuid();
        String filename = multipartFile.getOriginalFilename();
        Long size = multipartFile.getSize();
        InputStream inputStream = multipartFile.getInputStream();
        StorageResource storageResource = storageResourceService.createStorageResourceFile(storageId, parentResourceId, encodedFilename, inputStream);
        // saves object info
        StorageFileEntity storageFileEntity = StorageFileEntity.builder()
                .fileId(IdGenerator.uuid())
                .filename(filename)
                .size(size)
                .createdAt(Instant.now())
                .storageId(storageId)
                .resourceId(storageResource.getResourceId())
                .build();
        StorageFileEntity savedStorageFileEntity = storageFileRepository.saveAndFlush(storageFileEntity);
        return StorageFile.from(savedStorageFileEntity);
    }

    @Transactional
    public void attachStorageFile(String fileId, String targetType, String targetId) {
        StorageFileEntity storageFileEntity = storageFileRepository.findById(fileId).orElseThrow();
        storageFileEntity.setTargetType(targetType);
        storageFileEntity.setTargetId(targetId);
        storageFileRepository.saveAndFlush(storageFileEntity);
    }

    @Transactional
    public void detachStorageFile(String fileId) {
        StorageFileEntity storageFileEntity = storageFileRepository.findById(fileId).orElseThrow();
        storageFileEntity.setTargetType(null);
        storageFileEntity.setTargetId(null);
        storageFileRepository.saveAndFlush(storageFileEntity);
    }

    @Deprecated
    public void downloadStorageFile(String fileId, HttpServletResponse response) {
        StorageFileEntity storageFileEntity = storageFileRepository.findById(fileId).orElseThrow();
        String storageId = storageFileEntity.getStorageId();
        String resourceId = storageFileEntity.getResourceId();
        StorageResource storageResource = storageResourceService.getStorageResource(storageId, resourceId).orElseThrow();
        // gets resource input stream
        try (InputStream inputStream = storageResource.getInputStream()) {
            // sets response headers
            response.setContentType("application/octet-stream");
            String filename = storageFileEntity.getFilename();
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

    @Transactional
    public void deleteStorageFile(String fileId) {
        StorageFileEntity storageFileEntity = storageFileRepository.findById(fileId).orElseThrow();
        storageResourceService.deleteStorageResourceFile(storageFileEntity.getStorageId(), storageFileEntity.getResourceId());
        storageFileRepository.delete(storageFileEntity);
        storageFileRepository.flush();
    }

    /**
     * Gets storage files
     * @param storageFileSearch storage file search
     * @param pageable pageable
     * @return page of sotrage file
     */
    public Page<StorageFile> getStorageFiles(StorageFileSearch storageFileSearch, Pageable pageable) {
        Page<StorageFileEntity> storageFileEntityPage = storageFileRepository.findAll(storageFileSearch, pageable);
        List<StorageFile> storageFiles = storageFileEntityPage.getContent().stream()
                .map(StorageFile::from)
                .toList();
        long total = storageFileEntityPage.getTotalElements();
        return new PageImpl<>(storageFiles, pageable, total);
    }

    /**
     * Gets storage files
     * @param targetType target type
     * @param targetId target id
     * @return
     */
    public List<StorageFile> getStorageFiles(String targetType, String targetId) {
        return storageFileRepository.findAllByTarget(targetType, targetId).stream()
                .map(StorageFile::from)
                .collect(Collectors.toList());
    }

    /**
     * Gets storage file
     * @param fileId file id
     * @return storage file
     */
    public Optional<StorageFile> getStorageFile(String fileId) {
        return storageFileRepository.findById(fileId)
                .map(StorageFile::from);
    }

}
