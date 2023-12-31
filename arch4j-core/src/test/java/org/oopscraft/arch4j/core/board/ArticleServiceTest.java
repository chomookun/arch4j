package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.board.dao.ArticleEntity;
import org.oopscraft.arch4j.core.support.CoreTestSupport;
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
            .userName("test")
            .password("password")
            .build();

    ArticleFile testArticleFile = ArticleFile.builder()
            .articleId("article id")
            .fileId(null)
            .build();

    @Test
    @Order(1)
    void saveArticle() {
        Article savedArticle = articleService.saveArticle(testArticle, null);
        assertNotNull(entityManager.find(ArticleEntity.class, savedArticle.getArticleId()));
    }

    @Test
    @Order(2)
    void getArticle() {
        Article savedArticle = articleService.saveArticle(testArticle, null);
        Article article = articleService.getArticle(savedArticle.getArticleId()).orElse(null);
        assertNotNull(article);
    }

    @Test
    @Order(3)
    void deleteArticle() {
        Article savedArticle = articleService.saveArticle(testArticle, null);saveArticle();
        articleService.deleteArticle(savedArticle.getArticleId());
        assertNull(entityManager.find(ArticleEntity.class, savedArticle.getArticleId()));
    }

    @Test
    @Order(4)
    void getArticles() {
        Article savedArticle = articleService.saveArticle(testArticle, null);
        ArticleSearch articleSearch = ArticleSearch.builder()
                .boardId(savedArticle.getBoardId())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Article> articlePage = articleService.getArticles(articleSearch, pageable);
        assertTrue(articlePage.getContent().stream().anyMatch(e -> e.getBoardId().equals(articleSearch.getBoardId())));
    }

}