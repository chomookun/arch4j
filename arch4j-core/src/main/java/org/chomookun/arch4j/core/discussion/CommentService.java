package org.chomookun.arch4j.core.discussion;

import lombok.AllArgsConstructor;
import org.chomookun.arch4j.core.discussion.entity.CommentEntity;
import org.chomookun.arch4j.core.discussion.model.Comment;
import org.chomookun.arch4j.core.discussion.repository.CommentRepository;
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
                    .targetType(comment.getTargetType())
                    .targetId(comment.getTargetId())
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

    public List<Comment> getComments(String targetType, String targetId) {
        return commentRepository.findAllByTarget(targetType, targetId).stream()
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
