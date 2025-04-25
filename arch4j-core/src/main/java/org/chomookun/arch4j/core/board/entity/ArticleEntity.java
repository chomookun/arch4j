package org.chomookun.arch4j.core.board.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.board.model.Article;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.GenericEnumConverter;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "core_article",
    indexes = {
        @Index(name = "ix_board_id_created_at", columnList = "board_id, created_at"),
        @Index(name = "ix_title", columnList = "title"),
        @Index(name = "ix_user_id", columnList = "user_id")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleEntity extends BaseEntity {

    @Id
    @Column(name = "article_id", length = 32)
    private String articleId;

    @Column(name = "board_id", length = 32)
    private String boardId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "user_id", length = 32)
    private String userId;

    @Column(name = "title")
    private String title;

    @Column(name = "format", length = 16)
    private Article.Format format;

    @Column(name = "content", length = Integer.MAX_VALUE)
    @Lob
    private String content;

    @Converter(autoApply = true)
    public static class FormatConverter extends GenericEnumConverter<Article.Format> { }

}
