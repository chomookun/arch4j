package org.chomookun.arch4j.web.api.v1.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.model.*;
import org.chomookun.arch4j.core.board.ArticleService;
import org.chomookun.arch4j.core.board.BoardService;
import org.chomookun.arch4j.core.board.evaluator.BoardPermissionEvaluator;
import org.chomookun.arch4j.core.storage.model.StorageFile;
import org.chomookun.arch4j.web.api.v1.board.dto.*;
import org.chomookun.arch4j.core.common.data.PageableUtils;
import org.chomookun.arch4j.web.api.v1.storage.dto.StorageFileRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "board")
@RestController
@RequestMapping("api/v1/boards/{boardId}/articles")
@RequiredArgsConstructor
public class ArticleRestController {

    private final BoardService boardService;

    private final BoardPermissionEvaluator boardPermissionEvaluator;

    private final ArticleService articleService;

    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getArticles(
            @PathVariable("boardId") String boardId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @PageableDefault Pageable pageable
    ) {
        ArticleSearch articleSearch = ArticleSearch.builder()
                .boardId(boardId)
                .title(title)
                .content(content)
                .build();
        Page<Article> articlePage = articleService.getArticles(articleSearch, pageable);
        List<ArticleResponse> articleResponses = articlePage.getContent().stream()
                .map(ArticleResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, PageableUtils.toContentRange("article", pageable, articlePage.getTotalElements()))
                .body(articleResponses);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ArticleResponse> createArticle(
            @PathVariable("boardId") String boardId,
            @RequestBody ArticleRequest articleRequest
    ) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        Article article = Article.builder()
                .title(articleRequest.getTitle())
                .format(articleRequest.getFormat())
                .content(articleRequest.getContent())
                .boardId(boardId)
                .build();
        // file
        List<StorageFile> files = articleRequest.getArticleFiles().stream()
                .map(StorageFileRequest::toModel)
                .collect(Collectors.toList());
        article.setArticleFiles(files);
        // save
        Article savedArticle = articleService.saveArticle(article);
        return ResponseEntity.ok(ArticleResponse.from(savedArticle));
    }

    @PutMapping("{articleId}")
    @Transactional
    public ResponseEntity<ArticleResponse> modifyArticle(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @RequestBody ArticleRequest articleRequest
    ) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        Article article = articleService.getArticle(articleId).orElseThrow();
        article.setTitle(articleRequest.getTitle());
        article.setFormat(articleRequest.getFormat());
        article.setContent(articleRequest.getContent());
        // file
        List<StorageFile> files = articleRequest.getArticleFiles().stream()
                .map(StorageFileRequest::toModel)
                .collect(Collectors.toList());
        article.setArticleFiles(files);
        // save article
        Article savedArticle = articleService.saveArticle(article);
        return ResponseEntity.ok(ArticleResponse.from(savedArticle));
    }

    private void checkFileSizeLimit(Board board, MultipartFile[] multipartFiles) {
        if(board.getFileSizeLimit() != null) {
            for(MultipartFile multipartFile : multipartFiles) {
                long fileSize = multipartFile.getSize();
                if(fileSize/1024/1024 > board.getFileSizeLimit()) {
                    throw new RuntimeException("Exceed file size limit");
                }
            }
        }
    }

    @GetMapping("{articleId}")
    @Operation(summary = "get article")
    public ResponseEntity<ArticleResponse> getArticle(
            @Parameter(description = "board ID")
            @PathVariable("boardId")String boardId,
            @Parameter(description = "article ID")
            @PathVariable("articleId")String articleId
    ) {
        Article article = articleService.getArticle(articleId).orElseThrow();
        ArticleResponse articleResponse = ArticleResponse.from(article);
        return ResponseEntity.ok(articleResponse);
    }

    @DeleteMapping("{articleId}")
    @Transactional
    public ResponseEntity<Void> deleteArticle(
            @Parameter(description = "board ID")
            @PathVariable("boardId") String boardId,
            @Parameter(description = "article ID")
            @PathVariable("articleId") String articleId
    ) {
        Article article = articleService.getArticle(articleId).orElseThrow();
        articleService.deleteArticle(article.getArticleId());
        return ResponseEntity.ok().build();
    }

//    @GetMapping("{articleId}/file/{fileId}")
//    public ResponseEntity<Void> getArticleFile(
//            @Parameter(description = "board ID")
//            @PathVariable("boardId") String boardId,
//            @Parameter(description = "article ID")
//            @PathVariable("articleId") String articleId,
//            @Parameter(description = "file ID")
//            @PathVariable("fileId") String fileId,
//            HttpServletResponse response
//    ) {
//        ArticleFile articleFile = articleService.getArticleFile(articleId, fileId).orElseThrow();
//        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\";", articleFile.getFilename()));
//        try (InputStream inputStream = articleService.getArticleFileInputStream(articleFile)) {
//            StreamUtils.copy(inputStream, response.getOutputStream());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok().build();
//    }

}
