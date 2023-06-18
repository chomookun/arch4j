package org.oopscraft.arch4j.core.board.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.board.TextFormat;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.user.repository.UserEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "core_article")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleEntity extends SystemFieldEntity {

    @Id
    @Column(name = "article_id", length = 32)
    private String articleId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "content_format", length = 16)
    private TextFormat contentFormat;

    @NotBlank
    @Column(name = "content")
    @Lob
    private String content;

    @Column(name = "board_id")
    private String boardId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "comment_count")
    @Builder.Default
    private Integer commentCount = 0;

    @Column(name = "like_count")
    @Builder.Default
    private Integer likeCount = 0;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

}
