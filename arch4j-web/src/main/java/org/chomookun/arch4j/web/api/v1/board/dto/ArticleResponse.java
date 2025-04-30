package org.chomookun.arch4j.web.api.v1.board.dto;

import lombok.Builder;
import lombok.Data;
import org.chomookun.arch4j.core.board.model.Article;
import org.chomookun.arch4j.web.api.v1.storage.dto.StorageFileResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ArticleResponse {

    private String articleId;

    private String boardId;

    private LocalDateTime createdAt;

    private String userId;

    private String userName;

    private String userIcon;

    private String title;

    private Article.Format format;

    private String content;

    @Builder.Default
    private List<StorageFileResponse> articleFiles = new ArrayList<>();

    public static ArticleResponse from(Article article) {
        ArticleResponse articleResponse =  ArticleResponse.builder()
                .articleId(article.getArticleId())
                .boardId(article.getBoardId())
                .createdAt(article.getCreatedAt())
                .userId(article.getUserId())
                .title(article.getTitle())
                .format(article.getFormat())
                .content(article.getContent())
                .articleFiles(article.getFiles().stream().map(StorageFileResponse::from).collect(Collectors.toList()))
                .build();
        if (article.getUser() != null) {
            articleResponse.setUserName(article.getUser().getName());
            articleResponse.setUserIcon(article.getUser().getIcon());
        }
        return articleResponse;
    }

}
