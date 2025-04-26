package org.chomookun.arch4j.core.discussion.model;

import lombok.*;
import org.chomookun.arch4j.core.discussion.entity.CommentEntity;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment {

    private String commentId;

    private String targetType;

    private String targetId;

    private Instant createdAt;

    private String parentCommentId;

    private String userId;

    private String userName;

    private String userIcon;

    private String content;

    public static Comment from(CommentEntity commentEntity) {
        return Comment.builder()
                .commentId(commentEntity.getCommentId())
                .targetType(commentEntity.getTargetType())
                .targetId(commentEntity.getTargetId())
                .createdAt(commentEntity.getCreatedAt())
                .parentCommentId(commentEntity.getParentCommentId())
                .userId(commentEntity.getUserId())
                .content(commentEntity.getContent())
                .build();
    }

}
