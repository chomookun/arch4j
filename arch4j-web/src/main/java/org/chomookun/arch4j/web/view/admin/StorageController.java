package org.chomookun.arch4j.web.view.admin;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.storage.StorageObjectService;
import org.chomookun.arch4j.core.storage.StorageResourceService;
import org.chomookun.arch4j.core.storage.StorageService;
import org.chomookun.arch4j.core.storage.client.StorageClientDefinitionRegistry;
import org.chomookun.arch4j.core.storage.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("admin/storage")
@PreAuthorize("hasAuthority('admin.storage')")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    private final StorageResourceService storageResourceService;

    private final StorageObjectService storageObjectService;

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

    @PostMapping("create-storage-folder")
    @ResponseBody
    public StorageResourceInfo createStorageFolder(
            @RequestParam("storageId") String storageId,
            @RequestParam("parentResourceId") String parentResourceId,
            @RequestParam("name") String name
    ) {
        StorageResource storageResource = storageResourceService.createStorageFolder(storageId, parentResourceId, name);
        return StorageResourceInfo.from(storageResource);
    }

    @PostMapping("upload-storage-files")
    @ResponseBody
    public List<StorageResourceInfo> createStorageFiles(
            @RequestParam("storageId") String storageId,
            @RequestParam("parentResourceId") String parentResourceId,
            @RequestParam("files") MultipartFile[] multipartFiles
    ) throws IOException {
        List<StorageResourceInfo> storageResourceInfos = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String name = multipartFile.getOriginalFilename();
            StorageResource storageResource = storageResourceService.createStorageFile(storageId, parentResourceId, name, multipartFile.getInputStream());
            storageResourceInfos.add(StorageResourceInfo.from(storageResource));
        }
        return storageResourceInfos;
    }

    @DeleteMapping("delete-storage-resource")
    @ResponseBody
    @Transactional
    public void deleteStorageResource(@RequestParam("storageId") String storageId, @RequestParam("resourceId") String resourceId) {
        StorageResource storageResource = storageResourceService.getStorageResource(storageId, resourceId);
        switch (storageResource.getType()) {
            case FOLDER -> storageResourceService.deleteStorageFolder(storageId, storageResource.getResourceId());
            case FILE -> storageResourceService.deleteStorageFile(storageId, storageResource.getResourceId());
            default -> throw new RuntimeException("Unknown resource type: " + storageResource.getType());
        }
    }

    @GetMapping("download-storage-file")
    @ResponseBody
    public void getStorageFile(@RequestParam("storageId") String storageId, @RequestParam("resourceId") String resourceId, HttpServletResponse response) {
        StorageResource storageResource = storageResourceService.getStorageResource(storageId, resourceId);
        response.setContentType("application/octet-stream");
        String encodedFilename = URLEncoder.encode(storageResource.getFilename(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition",String.format("attachment; filename=\"%s\";", encodedFilename));
        try (InputStream inputStream = storageResource.getInputStream()) {
            StreamUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping("get-storage-objects")
    @ResponseBody
    public Page<StorageObject> getStorageObjects(
            @RequestParam(value = "storageId", required = false) String storageId,
            @PageableDefault Pageable pageable
    ) {
        StorageObjectSearch storageObjectSearch = StorageObjectSearch.builder()
                .storageId(storageId)
                .build();
        return storageObjectService.getStorageObjects(storageObjectSearch, pageable);
    }

    @PostMapping("create-storage-object")
    @ResponseBody
    public List<StorageObject> createStorageObject(
            @RequestParam("group") String group,
            @RequestParam("storageId") String storageId,
            @RequestPart(value = "files", required = false) MultipartFile[] multipartFiles
    ) throws IOException {
        List<StorageObject> storageObjects = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String filename = multipartFile.getOriginalFilename();
            StorageObject storageObject = storageObjectService.createStorageObject(group, filename, multipartFile.getInputStream(), storageId);
            storageObjects.add(storageObject);
        }
        return storageObjects;
    }

}
