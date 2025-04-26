package org.chomookun.arch4j.core.discussion.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import java.time.Instant;

@Entity
@Table(
        name = "core_comment",
        indexes = {
                @Index(name = "idx_core_comment_target", columnList = "target_type, target_id")
        }
)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentEntity extends BaseEntity {

    @Id
    @Column(name = "comment_id", length = 32)
    private String commentId;

    @Column(name = "target_type", length = 32)
    private String targetType;

    @Column(name = "target_id", length = 128)
    private String targetId;

    @Column(name = "discussion_id", length = 32)
    private String discussionId;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "parent_comment_id", length = 32)
    private String parentCommentId;

    @Column(name = "user_id", length = 32)
    private String userId;

    @Column(name = "content", length = Integer.MAX_VALUE)
    private String content;

}
