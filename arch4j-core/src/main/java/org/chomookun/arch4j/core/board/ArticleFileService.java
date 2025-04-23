package org.chomookun.arch4j.core.board;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.model.Board;
import org.chomookun.arch4j.core.storage.StorageFileService;
import org.chomookun.arch4j.core.storage.model.StorageFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleFileService {

    private static final String STORAGE_ID = "board";

    private static final String TARGET_TYPE = "board:article";

    private final BoardService boardService;

    private final StorageFileService storageFileService;

    public StorageFile createArticleFile(String articleId, MultipartFile multipartFile) throws IOException {
        return storageFileService.uploadStorageFile(multipartFile, STORAGE_ID);
    }

    public void attachArticleFile(String articleId, String fileId) {
        storageFileService.attachStorageFile(fileId, TARGET_TYPE, articleId);
    }

    public void detachArticleFile(String articleId, String fileId) {
        storageFileService.detachStorageFile(fileId);
    }

    public List<StorageFile> getArticleFiles(String articleId) {
        return storageFileService.getStorageFiles(TARGET_TYPE, articleId);
    }

    public Optional<StorageFile> getArticleFile(String articleId, String fileId) {
        return storageFileService.getStorageFile(fileId);
    }

    @Deprecated
    public void downloadBoardFile(String boardId, String fileId, HttpServletResponse response) throws IOException {
        Board board = boardService.getBoard(boardId).orElseThrow();
        storageFileService.downloadStorageFile(fileId, response);
    }

}
