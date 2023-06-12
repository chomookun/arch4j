package org.oopscraft.arch4j.core.board.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "core_article_file")
@IdClass(ArticleFileEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleFileEntity extends SystemFieldEntity {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pk implements Serializable {
        private String articleId;
        private String fileId;
    }

    @Id
    @Column(name = "article_id", length = 32)
    private String articleId;

    @Id
    @Column(name = "file_id", length = 32)
    private String fileId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "filename")
    private String filename;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "length")
    @Builder.Default
    private Long length = 0L;

}

