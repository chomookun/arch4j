package org.chomookun.arch4j.core.comment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import java.time.Instant;

@Entity
@Table(name = "core_comment")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentEntity extends BaseEntity {

    @Id
    @Column(name = "comment_id", length = 32)
    private String commentId;

    @Column(name = "discussion_id", length = 32)
    private String discussionId;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "thread", length = 128)
    private String thread;

    @Column(name = "parent_comment_id", length = 32)
    private String parentCommentId;

    @Column(name = "user_id", length = 32)
    private String userId;

    @Column(name = "content", length = Integer.MAX_VALUE)
    private String content;

}
