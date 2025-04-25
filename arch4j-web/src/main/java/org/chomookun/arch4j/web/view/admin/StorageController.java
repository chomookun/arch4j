package org.chomookun.arch4j.web.view.admin;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.storage.StorageFileService;
import org.chomookun.arch4j.core.storage.StorageResourceService;
import org.chomookun.arch4j.core.storage.StorageService;
import org.chomookun.arch4j.core.storage.client.StorageClientDefinitionRegistry;
import org.chomookun.arch4j.core.storage.model.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("admin/storage")
@PreAuthorize("hasAuthority('admin.storage')")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    private final StorageResourceService storageResourceService;

    private final StorageFileService storageFileService;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("admin/storage");
        modelAndView.addObject("storageClientDefinitions", StorageClientDefinitionRegistry.getStorageClientDefinitions());
        return modelAndView;
    }

    @GetMapping("get-storages")
    @ResponseBody
    public Page<Storage> getStorages(StorageSearch storageSearch, Pageable pageable) {
        return storageService.getStorages(storageSearch, pageable);
    }

    @GetMapping("get-storage")
    @ResponseBody
    public Storage getStorage(@RequestParam("storageId") String storageId) {
        return storageService.getStorage(storageId)
                .orElseThrow();
    }

    @PostMapping("save-storage")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.storage:edit')")
    @Transactional
    public Storage saveStorage(@RequestBody Storage storage) {
        return storageService.saveStorage(storage);
    }

    @DeleteMapping("delete-storage")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.storage:edit')")
    @Transactional
    public void deleteStorage(@RequestParam("storageId") String storageId) {
        storageService.deleteStorage(storageId);
    }

    @GetMapping("get-storage-resource-info")
    @ResponseBody
    public StorageResourceInfo getStorageResourceInfo(@RequestParam("storageId") String storageId, @RequestParam("resourceId") String resourceId) {
        return storageResourceService.getStorageResourceInfo(storageId, resourceId);
    }

    @PostMapping("create-storage-resource-folder")
    @ResponseBody
    public StorageResourceInfo createStorageFolder(
            @RequestParam("storageId") String storageId,
            @RequestParam("parentResourceId") String parentResourceId,
            @RequestParam("name") String name
    ) {
        StorageResource storageResource = storageResourceService.createStorageResourceFolder(storageId, parentResourceId, name);
        return StorageResourceInfo.from(storageResource);
    }

    @PostMapping("upload-storage-resources")
    @ResponseBody
    public List<StorageResourceInfo> uploadStorageResources(
            @RequestParam("storageId") String storageId,
            @RequestParam("parentResourceId") String parentResourceId,
            @RequestParam("files") MultipartFile[] multipartFiles
    ) throws IOException {
        List<StorageResourceInfo> storageResourceInfos = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String name = multipartFile.getOriginalFilename();
            StorageResource storageResource = storageResourceService.createStorageResourceFile(storageId, parentResourceId, name, multipartFile.getInputStream());
            storageResourceInfos.add(StorageResourceInfo.from(storageResource));
        }
        return storageResourceInfos;
    }

    @DeleteMapping("delete-storage-resource")
    @ResponseBody
    @Transactional
    public void deleteStorageResource(@RequestParam("storageId") String storageId, @RequestParam("resourceId") String resourceId) {
        StorageResource storageResource = storageResourceService.getStorageResource(storageId, resourceId).orElseThrow();
        switch (storageResource.getType()) {
            case FOLDER -> storageResourceService.deleteStorageResourceFolder(storageId, storageResource.getResourceId());
            case FILE -> storageResourceService.deleteStorageResourceFile(storageId, storageResource.getResourceId());
            default -> throw new RuntimeException("Unknown resource type: " + storageResource.getType());
        }
    }

    @GetMapping("download-storage-resource")
    @ResponseBody
    public ResponseEntity<Resource> downloadStorageResource(@RequestParam("storageId") String storageId, @RequestParam("resourceId") String resourceId, HttpServletResponse response) throws IOException {
        StorageResource storageResource = storageResourceService.getStorageResource(storageId, resourceId).orElseThrow();
        String filename = storageResource.getFilename();
        MediaType mediaType = MediaTypeFactory
                .getMediaType(filename)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build();
        Resource resource = new InputStreamResource(storageResource.getInputStream());
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(resource);
    }

    @GetMapping("get-storage-files")
    @ResponseBody
    public Page<StorageFile> getStorageFiles(
            @RequestParam(value = "targetType", required = false) String targetType,
            @RequestParam(value = "targetId", required = false) String targetId,
            @RequestParam(value = "filename", required = false) String filename,
            @PageableDefault Pageable pageable
    ) {
        StorageFileSearch storageObjectSearch = StorageFileSearch.builder()
                .targetType(targetType)
                .targetId(targetId)
                .filename(filename)
                .build();
        return storageFileService.getStorageFiles(storageObjectSearch, pageable);
    }

    @PostMapping("upload-storage-files")
    @ResponseBody
    @Transactional
    public List<StorageFile> uploadStorageFiles(
            @RequestPart(value = "files", required = false) MultipartFile[] multipartFiles,
            @RequestParam("storageId") String storageId,
            @RequestParam(value = "targetType", required = false) String targetType,
            @RequestParam(value = "targetId", required = false) String targetId
    ) throws IOException {
        List<StorageFile> storageObjects = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            // upload
            StorageFile storageFile = storageFileService.uploadStorageFile(multipartFile, storageId, targetType);
            // attach
            storageFileService.attachStorageFile(storageFile.getFileId(), targetType, targetId);
            // adds to result
            storageFile.setTargetType(targetType);
            storageFile.setTargetId(targetId);
            storageObjects.add(storageFile);
        }
        return storageObjects;
    }

    @GetMapping("download-storage-file")
    @ResponseBody
    public ResponseEntity<Resource> downloadStorageFile(@RequestParam("fileId") String fileId) throws IOException {
        StorageFile storageFile = storageFileService.getStorageFile(fileId).orElseThrow();
        String filename = storageFile.getFilename();
        MediaType mediaType = MediaTypeFactory
                .getMediaType(filename)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build();
        StorageResource storageResource = storageResourceService.getStorageResource(storageFile).orElseThrow();
        Resource resource = new InputStreamResource(storageResource.getInputStream());
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(resource);
    }

    @DeleteMapping("delete-storage-file")
    @ResponseBody
    @Transactional
    public void deleteStorageFile(@RequestParam("fileId") String fileId) {
        storageFileService.deleteStorageFile(fileId);
    }

}
