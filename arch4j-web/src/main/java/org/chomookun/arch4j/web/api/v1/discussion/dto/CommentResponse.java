package org.chomookun.arch4j.web.api.v1.discussion.dto;

import lombok.Builder;
import lombok.Data;
import org.chomookun.arch4j.core.discussion.model.Comment;

import java.time.Instant;

@Data
@Builder
public class CommentResponse {

    private String commentId;

    private String targetType;

    private String targetId;

    private Instant createdAt;

    private String parenCommentId;

    private String userId;

    private String userName;

    private String userIcon;

    private String content;

    private boolean canUpdate;

    private boolean canDelete;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .targetType(comment.getTargetType())
                .targetId(comment.getTargetId())
                .createdAt(comment.getCreatedAt())
                .parenCommentId(comment.getParentCommentId())
                .userId(comment.getUserId())
                .userName(comment.getUserName())
                .userIcon(comment.getUserIcon())
                .content(comment.getContent())
                .build();
    }

}
