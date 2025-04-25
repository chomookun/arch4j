package org.chomookun.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.entity.*;
import org.chomookun.arch4j.core.board.model.*;
import org.chomookun.arch4j.core.board.repository.ArticleRepository;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.common.data.ValidationUtil;
import org.chomookun.arch4j.core.storage.model.StorageFile;
import org.chomookun.arch4j.core.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final UserService userService;

    private final ArticleFileService articleFileService;

    public String generateArticleId() {
        return IdGenerator.uuid();
    }

    @Transactional
    public Article saveArticle(Article article) {
        ValidationUtil.validate(article);
        ArticleEntity articleEntity = Optional.ofNullable(article.getArticleId())
                .flatMap(articleRepository::findById).orElse(
                        ArticleEntity.builder()
                                .articleId(generateArticleId())
                                .createdAt(LocalDateTime.now())
                                .userId(article.getUserId())
                                .build());
        articleEntity.setTitle(article.getTitle());
        articleEntity.setFormat(article.getFormat());
        articleEntity.setContent(article.getContent());
        articleEntity.setBoardId(article.getBoardId());
        // files
        List<StorageFile> oldArticleFiles = articleFileService.getArticleFiles(articleEntity.getArticleId());
        List<StorageFile> newArticleFiles = article.getArticleFiles();
        newArticleFiles.stream()
                .filter(newOne ->
                        oldArticleFiles.stream().noneMatch(oldOne -> Objects.equals(oldOne.getFileId(), newOne.getFileId())))
                .forEach(it -> {
                    articleFileService.attachArticleFile(articleEntity.getArticleId(), it.getFileId());
                });
        oldArticleFiles.stream()
                .filter(oldOne ->
                        newArticleFiles.stream().noneMatch(currentFile -> Objects.equals(currentFile.getFileId(), oldOne.getFileId())))
                .forEach(it -> {
                    articleFileService.detachArticleFile(articleEntity.getArticleId(), it.getFileId());
                });
        // saves and return
        ArticleEntity savedArticleEntity = articleRepository.saveAndFlush(articleEntity);
        return getArticle(savedArticleEntity.getArticleId()).orElseThrow();
    }

    public Page<Article> getArticles(ArticleSearch articleSearch, Pageable pageable) {
        Page<ArticleEntity> articleEntityPage = articleRepository.findAll(articleSearch, pageable);
        List<Article> articles = articleEntityPage.getContent().stream()
                .map(Article::from)
                .toList();
        articles.forEach(this::populateArticle);
        long total = articleEntityPage.getTotalElements();
        return new PageImpl<>(articles, pageable, total);
    }

    public Optional<Article> getArticle(String articleId) {
        Article article = articleRepository.findById(articleId)
                .map(Article::from)
                .orElse(null);
        if (article != null) {
            populateArticle(article);
            List<StorageFile> files = articleFileService.getArticleFiles(article.getArticleId());
            article.setArticleFiles(files);
        }
        return Optional.ofNullable(article);
    }

    private void populateArticle(Article article) {
        if (article.getUserId() != null) {
             userService.getUser(article.getUserId()).ifPresent(article::setUser);
        }
    }

    @Transactional
    public void deleteArticle(String articleId) {
        articleRepository.deleteById(articleId);
        articleRepository.flush();
        articleFileService.getArticleFiles(articleId).forEach(it -> {
            articleFileService.detachArticleFile(articleId, it.getFileId());
        });
    }

}
