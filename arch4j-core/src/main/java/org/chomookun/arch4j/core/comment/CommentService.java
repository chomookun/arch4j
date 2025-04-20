package org.chomookun.arch4j.core.comment;

import lombok.AllArgsConstructor;
import org.chomookun.arch4j.core.comment.entity.CommentEntity;
import org.chomookun.arch4j.core.comment.model.Comment;
import org.chomookun.arch4j.core.comment.repository.CommentRepository;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserService userService;

    @Transactional
    public Comment saveComment(Comment comment) {
        CommentEntity commentEntity;
        if (comment.getCommentId() == null) {
            commentEntity = CommentEntity.builder()
                    .commentId(IdGenerator.uuid())
                    .thread(comment.getThread())
                    .createdAt(Instant.now())
                    .userId(comment.getUserId())
                    .parentCommentId(comment.getParentCommentId())
                    .build();
        } else {
            commentEntity = commentRepository.findById(comment.getCommentId()).orElseThrow();
        }
        commentEntity.setContent(comment.getContent());
        CommentEntity savedCommentEntity = commentRepository.save(commentEntity);
        Comment savedComment = Comment.from(savedCommentEntity);
        populateComment(savedComment);
        return savedComment;
    }

    public List<Comment> getComments(String thread) {
        return commentRepository.findAllByThread(thread).stream()
                .map(Comment::from)
                .map(this::populateComment)
                .collect(Collectors.toList());
    }

    public Optional<Comment> getComment(String commentId) {
        return commentRepository.findById(commentId)
                .map(Comment::from)
                .map(this::populateComment);
    }

    private Comment populateComment(Comment comment) {
        if (comment.getUserId() != null) {
            userService.getUser(comment.getUserId()).ifPresent(user -> {
                comment.setUserId(user.getUserId());
                comment.setUserName(user.getName());
                comment.setUserIcon(user.getIcon());
            });
        }
        return comment;
    }

    @Transactional
    public void deleteComment(String commentId) {
        commentRepository.deleteById(commentId);
    }

}
