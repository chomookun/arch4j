package org.chomookun.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.ArticleCommentService;
import org.chomookun.arch4j.core.discussion.model.Comment;
import org.chomookun.arch4j.core.security.SecurityUtils;
import org.chomookun.arch4j.web.api.v1.discussion.dto.CommentRequest;
import org.chomookun.arch4j.web.api.v1.discussion.dto.CommentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Tag(name = "board")
@RestController
@RequestMapping("api/v1/boards/{boardId}/articles/{articleId}/comments")
@RequiredArgsConstructor
public class ArticleCommentRestController {

    private final ArticleCommentService articleCommentService;

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getArticleComments(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId
    ) {
        List<CommentResponse> commentResponses = articleCommentService.getArticleComments(articleId).stream()
                .map(CommentResponse::from)
                .map(this::populateCommentResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentResponses);
    }

    @PostMapping
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponse> createArticleComment(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @RequestBody CommentRequest commentRequest
    ) {
        Comment comment = Comment.builder()
                .userId(SecurityUtils.getCurrentUserId())
                .parentCommentId(commentRequest.getParentCommentId())
                .content(commentRequest.getContent())
                .build();
        Comment savedComment = articleCommentService.saveArticleComment(articleId, comment);
        CommentResponse savedCommentResponse = CommentResponse.from(savedComment);
        populateCommentResponse(savedCommentResponse);
        return ResponseEntity.ok(savedCommentResponse);
    }

    @PutMapping("{commentId}")
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponse> modifyArticleComment(
            @RequestBody CommentRequest commentRequest,
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @PathVariable("commentId") String commentId
    ) {
        Comment comment = articleCommentService.getArticleComment(articleId, commentId).orElseThrow();
        comment.setContent(commentRequest.getContent());
        Comment savedComment = articleCommentService.saveArticleComment(articleId, comment);
        CommentResponse savedCommentResponse = CommentResponse.from(savedComment);
        populateCommentResponse(savedCommentResponse);
        return ResponseEntity.ok(savedCommentResponse);
    }

    @GetMapping("{commentId}")
    public ResponseEntity<CommentResponse> getArticleComment(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @PathVariable("commentId") String commentId
    ) {
        Comment comment = articleCommentService.getArticleComment(articleId, commentId).orElseThrow();
        CommentResponse commentResponse = CommentResponse.from(comment);
        populateCommentResponse(commentResponse);
        return ResponseEntity.ok(commentResponse);
    }

    @DeleteMapping("{commentId}")
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteArticleComment(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @PathVariable("commentId") String commentId
    ) {
        articleCommentService.deleteArticleComment(articleId, commentId);
        return ResponseEntity.noContent().build();
    }

    private CommentResponse populateCommentResponse(CommentResponse commentResponse) {
        commentResponse.setCanUpdate(canUpdateComment(commentResponse));
        commentResponse.setCanDelete(canDeleteComment(commentResponse));
        return commentResponse;
    }

    private boolean canUpdateComment(CommentResponse commentResponse) {
        return isMyComment(commentResponse);
    }

    private boolean canDeleteComment(CommentResponse commentResponse) {
        return isMyComment(commentResponse);
    }

    private boolean isMyComment(CommentResponse commentResponse) {
        if (commentResponse.getUserId() != null) {
            return Objects.equals(commentResponse.getUserId(), SecurityUtils.getCurrentUserId());
        }
        return false;
    }

}
