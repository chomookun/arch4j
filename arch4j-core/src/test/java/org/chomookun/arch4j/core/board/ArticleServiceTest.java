package org.chomookun.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.board.entity.ArticleEntity;
import org.chomookun.arch4j.core.board.model.Article;
import org.chomookun.arch4j.core.board.model.ArticleSearch;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class ArticleServiceTest extends CoreTestSupport {

    final ArticleService articleService;

    Article testArticle = Article.builder()
            .articleId(null)
            .title("test title")
            .content("test content")
            .boardId("test")
            .userId("test")
            .build();

    @Test
    @Order(1)
    void saveArticle() {
        Article savedArticle = articleService.saveArticle(testArticle);
        assertNotNull(entityManager.find(ArticleEntity.class, savedArticle.getArticleId()));
    }

    @Test
    @Order(2)
    void getArticle() {
        Article savedArticle = articleService.saveArticle(testArticle);
        Article article = articleService.getArticle(savedArticle.getArticleId()).orElse(null);
        assertNotNull(article);
    }

    @Test
    @Order(3)
    void deleteArticle() {
        Article savedArticle = articleService.saveArticle(testArticle);saveArticle();
        articleService.deleteArticle(savedArticle.getArticleId());
        assertNull(entityManager.find(ArticleEntity.class, savedArticle.getArticleId()));
    }

    @Test
    @Order(4)
    void getArticles() {
        Article savedArticle = articleService.saveArticle(testArticle);
        ArticleSearch articleSearch = ArticleSearch.builder()
                .boardId(savedArticle.getBoardId())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Article> articlePage = articleService.getArticles(articleSearch, pageable);
        assertTrue(articlePage.getContent().stream().anyMatch(e -> e.getBoardId().equals(articleSearch.getBoardId())));
    }

}