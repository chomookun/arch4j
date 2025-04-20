package org.chomookun.arch4j.web.api.v1.comment.dto;

import lombok.Builder;
import lombok.Data;
import org.chomookun.arch4j.core.comment.model.Comment;

import java.time.Instant;

@Data
@Builder
public class CommentResponse {

    private String commentId;

    private Instant createdAt;

    private String thread;

    private String parentCommentId;

    private String userId;

    private String userName;

    private String userIcon;

    private String content;

    private boolean canUpdate;

    private boolean canDelete;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .createdAt(comment.getCreatedAt())
                .thread(comment.getThread())
                .parentCommentId(comment.getParentCommentId())
                .userId(comment.getUserId())
                .userName(comment.getUserName())
                .userIcon(comment.getUserIcon())
                .content(comment.getContent())
                .build();
    }

}
