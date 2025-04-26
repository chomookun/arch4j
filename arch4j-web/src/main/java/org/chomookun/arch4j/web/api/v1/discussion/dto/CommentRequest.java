package org.chomookun.arch4j.web.api.v1.discussion.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequest {

    private String parentCommentId;

    private String userId;

    private String content;

}
