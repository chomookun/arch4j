package org.chomookun.arch4j.core.board.model;

import lombok.*;
import org.chomookun.arch4j.core.board.entity.ArticleVoteEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleVote {

    private String articleId;

    private String userId;

    @Builder.Default
    private Long point = 0L;

    public static ArticleVote from(ArticleVoteEntity articleVoteEntity) {
        return ArticleVote.builder()
                .articleId(articleVoteEntity.getArticleId())
                .userId(articleVoteEntity.getUserId())
                .point(articleVoteEntity.getPoint())
                .build();
    }

}
