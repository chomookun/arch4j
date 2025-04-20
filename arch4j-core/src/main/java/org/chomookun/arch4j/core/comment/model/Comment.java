package org.chomookun.arch4j.core.comment.model;

import lombok.*;
import org.chomookun.arch4j.core.comment.entity.CommentEntity;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment {

    private String commentId;

    private Instant createdAt;

    private String thread;

    private String parentCommentId;

    private String userId;

    private String userName;

    private String userIcon;

    private String content;

    public static Comment from(CommentEntity commentEntity) {
        return Comment.builder()
                .commentId(commentEntity.getCommentId())
                .createdAt(commentEntity.getCreatedAt())
                .thread(commentEntity.getThread())
                .parentCommentId(commentEntity.getParentCommentId())
                .userId(commentEntity.getUserId())
                .content(commentEntity.getContent())
                .build();
    }

}
