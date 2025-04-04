package org.chomookun.arch4j.core.board.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_article_vote")
@IdClass(ArticleVoteEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleVoteEntity extends BaseEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pk implements Serializable {
        private String articleId;
        private String userId;
    }

    @Id
    @Column(name = "article_id", length = 32)
    private String articleId;

    @Id
    @Column(name = "user_id", length = 32)
    private String userId;

    @Column(name = "point")
    @Builder.Default
    private Long point = 0L;

}
