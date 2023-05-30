package org.oopscraft.arch4j.core.comment.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.user.repository.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "core_comment")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentEntity extends SystemFieldEntity {

    @Id
    @Column(name = "id", length = 64)
    private String id;

    @Column(name = "owner_type")
    private String ownerType;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "content")
    @Lob
    private String content;

    @Column(name = "userId")
    private String userId;

    @Column(name = "like_count")
    @Builder.Default
    private Long likeCount = 0L;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = CommentEntity_.USER_ID, insertable = false, updatable = false)
    private UserEntity user;

}
