package org.chomookun.arch4j.core.board.model;

import lombok.*;
import org.chomookun.arch4j.core.board.entity.ArticleEntity;
import org.chomookun.arch4j.core.storage.model.StorageFile;
import org.chomookun.arch4j.core.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Article {

    private String articleId;

    private String boardId;

    private LocalDateTime createdAt;

    private String userId;

    private String title;

    private Article.Format format;

    private String content;

    private User user;

    @Builder.Default
    private List<StorageFile> articleFiles = new ArrayList<>();

    public enum Format { TEXT, MARKDOWN }

    public static Article from(ArticleEntity articleEntity) {
        return Article.builder()
                .articleId(articleEntity.getArticleId())
                .boardId(articleEntity.getBoardId())
                .createdAt(articleEntity.getCreatedAt())
                .userId(articleEntity.getUserId())
                .title(articleEntity.getTitle())
                .format(articleEntity.getFormat())
                .content(articleEntity.getContent())
                .build();
    }

}
