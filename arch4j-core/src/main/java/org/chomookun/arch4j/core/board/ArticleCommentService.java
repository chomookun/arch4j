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

    private final CommentService commentService;

    public Comment saveArticleComment(String articleId, Comment comment) {
        String thread = String.format("article:%s", articleId);
        comment.setThread(thread);
        return commentService.saveComment(comment);
    }

    public List<Comment> getArticleComments(String articleId) {
        String thread = String.format("article:%s", articleId);
        return commentService.getComments(thread);
    }

    public Optional<Comment> getArticleComment(String articleId, String commentId) {
        return commentService.getComment(commentId);
    }

    public void deleteArticleComment(String articleId, String commentId) {
        String thread = String.format("article:%s", articleId);
        commentService.deleteComment(commentId);
    }

}
