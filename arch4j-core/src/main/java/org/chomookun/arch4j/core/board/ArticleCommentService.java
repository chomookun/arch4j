package org.chomookun.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.discussion.CommentService;
import org.chomookun.arch4j.core.discussion.model.Comment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleCommentService {

    private static final String TARGET_TYPE = "board.article";

    private final CommentService commentService;

    public Comment saveArticleComment(String articleId, Comment comment) {
        comment.setTargetType(TARGET_TYPE);
        comment.setTargetId(articleId);
        return commentService.saveComment(comment);
    }

    public List<Comment> getArticleComments(String articleId) {
        return commentService.getComments(TARGET_TYPE, articleId);
    }

    public Optional<Comment> getArticleComment(String articleId, String commentId) {
        return commentService.getComment(commentId);
    }

    public void deleteArticleComment(String articleId, String commentId) {
        commentService.deleteComment(commentId);
    }

}
