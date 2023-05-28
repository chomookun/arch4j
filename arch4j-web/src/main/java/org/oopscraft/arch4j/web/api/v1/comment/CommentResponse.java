package org.oopscraft.arch4j.web.api.v1.comment;

import lombok.*;
import org.oopscraft.arch4j.core.comment.Comment;
import org.oopscraft.arch4j.core.comment.CommentTarget;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponse {

    private String id;

    private String parentId;

    private CommentTarget target;

    private String targetId;

    private String content;

    private String userId;

    private String userName;

    /**
     * factory method
     * @param comment comment
     * @return comment response
     */
    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .parentId(comment.getParentId())
                .target(comment.getTarget())
                .targetId(comment.getTargetId())
                .content(comment.getContent())
                .userId(comment.getUserId())
                .userName(comment.getUserName())
                .build();
    }

}
