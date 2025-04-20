package org.chomookun.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.entity.*;
import org.chomookun.arch4j.core.board.model.*;
import org.chomookun.arch4j.core.board.repository.ArticleFileRepository;
import org.chomookun.arch4j.core.board.repository.ArticleRepository;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.common.data.ValidationUtil;
import org.chomookun.arch4j.core.comment.CommentService;
import org.chomookun.arch4j.core.storage.StorageResourceService;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.core.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final ArticleFileRepository articleFileRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final StorageResourceService storageResourceService;

    private final CommentService commentService;

    @Transactional
    public Article saveArticle(Article article, List<MultipartFile> files) {
        ValidationUtil.validate(article);
        ArticleEntity articleEntity = Optional.ofNullable(article.getArticleId())
                .flatMap(articleRepository::findById).orElse(
                        ArticleEntity.builder()
                                .articleId(IdGenerator.uuid())
                                .createdAt(LocalDateTime.now())
                                .userId(article.getUserId())
                                .build());
        articleEntity.setTitle(article.getTitle());
        articleEntity.setFormat(article.getFormat());
        articleEntity.setContent(article.getContent());
        articleEntity.setBoardId(article.getBoardId());
        articleEntity = articleRepository.saveAndFlush(articleEntity);
        // new file
        for(ArticleFile articleFile : article.getArticleFiles()) {
            ArticleFileEntity.Pk pk = ArticleFileEntity.Pk.builder()
                    .articleId(articleEntity.getArticleId())
                    .fileId(articleFile.getFileId())
                    .build();
            ArticleFileEntity articleFileEntity = articleFileRepository.findById(pk).orElse(null);
            if(articleFileEntity == null) {
                articleFileEntity = ArticleFileEntity.builder()
                        .articleId(articleEntity.getArticleId())
                        .fileId(IdGenerator.uuid())
                        .filename(articleFile.getFilename())
                        .contentType(articleFile.getContentType())
                        .length(articleFile.getLength())
                        .build();
                articleFileRepository.saveAndFlush(articleFileEntity);
                // upload file (same filename)
                for(MultipartFile file : files) {
                    if(Objects.equals(file.getOriginalFilename(), articleFileEntity.getFilename())){
                        try {
                            storageResourceService.createStorageFile("board", null, file.getName(), file.getInputStream());
                        }catch(Throwable ignore){}
                        break;
                    }
                }
            }
        }
        // deleted file
        for(ArticleFileEntity articleFileEntity : articleFileRepository.findAllByArticleIdOrderByCreatedAtAsc(articleEntity.getArticleId())) {
            if(article.getArticleFiles().stream().noneMatch(e -> articleFileEntity.getFilename().equals(e.getFilename()))){
                articleFileRepository.delete(articleFileEntity);
                articleFileRepository.flush();
                storageResourceService.deleteStorageFile("board", articleFileEntity.getFilename());
            }
        }
        // sets files
        article.setArticleFiles(articleFileRepository.findAllByArticleIdOrderByCreatedAtAsc(articleEntity.getArticleId()).stream()
                .map(ArticleFile::from)
                .collect(Collectors.toList()));
        // saves and returns
        article = Article.from(articleEntity);
        return article;
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
        Article article = articleRepository.findById(articleId).map(Article::from).orElseThrow();
        article.setArticleFiles(articleFileRepository.findAllByArticleIdOrderByCreatedAtAsc(article.getArticleId()).stream()
                .map(ArticleFile::from)
                .collect(Collectors.toList()));
        populateArticle(article);
        return Optional.of(article);
    }

    private void populateArticle(Article article) {
        if (article.getUserId() != null) {
             userService.getUser(article.getUserId()).ifPresent(article::setUser);
        }
    }

    @Transactional
    public void deleteArticle(String articleId) {
        articleFileRepository.deleteByArticleId(articleId);
        articleFileRepository.flush();
        articleRepository.deleteById(articleId);
        articleRepository.flush();
    }

    public Optional<ArticleFile> getArticleFile(String articleId, String fileId) {
        return articleFileRepository.findById(new ArticleFileEntity.Pk(articleId, fileId))
                .map(ArticleFile::from);
    }

    public InputStream getArticleFileInputStream(ArticleFile articleFile) {
        try {
            return storageResourceService.getStorageResource("board", articleFile.getFilename()).getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
