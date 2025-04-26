package org.chomookun.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.ArticleFileService;
import org.chomookun.arch4j.core.board.BoardService;
import org.chomookun.arch4j.core.board.model.Board;
import org.chomookun.arch4j.core.storage.StorageResourceService;
import org.chomookun.arch4j.core.storage.model.StorageFile;
import org.chomookun.arch4j.core.storage.model.StorageResource;
import org.chomookun.arch4j.web.api.v1.storage.dto.StorageFileResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Tag(name = "board")
@RestController
@RequestMapping("api/v1/boards/{boardId}/articles/{articleId}/files")
@RequiredArgsConstructor
public class ArticleFileRestController {

    private final BoardService boardService;

    private final ArticleFileService articleFileService;

    private final StorageResourceService storageResourceService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<StorageFileResponse> uploadArticleFile(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @RequestPart("file") MultipartFile multipartFile
    ) throws IOException{
        Board board = boardService.getBoard(boardId).orElseThrow();
        StorageFile storageFile = articleFileService.createArticleFile(articleId, multipartFile, board.getStorageId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{fileId}")
                .buildAndExpand(storageFile.getFileId())
                .toUri();
        StorageFileResponse storageFileResponse = StorageFileResponse.from(storageFile);
        return ResponseEntity.created(location)
                .body(storageFileResponse);
    }

    @GetMapping("{fileId}")
    public ResponseEntity<Resource> downloadArticleFile(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @PathVariable("fileId") String fileId
    ) throws IOException {
        StorageFile storageFile = articleFileService.getArticleFile(boardId, fileId).orElseThrow();
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

}
