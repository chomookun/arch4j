package org.chomookun.arch4j.web.api.v1.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.chomookun.arch4j.core.board.model.Article;
import org.chomookun.arch4j.web.api.v1.storage.dto.StorageFileRequest;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "Article Request")
public class ArticleRequest {

    private String articleId;

    private String title;

    private Article.Format format;

    private String content;

    @Builder.Default
    private List<StorageFileRequest> articleFiles = new ArrayList<>();

}
