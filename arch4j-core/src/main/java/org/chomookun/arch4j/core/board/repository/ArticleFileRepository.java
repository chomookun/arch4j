package org.chomookun.arch4j.core.board.repository;

import org.chomookun.arch4j.core.board.entity.ArticleFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleFileRepository extends JpaRepository<ArticleFileEntity, ArticleFileEntity.Pk> {

    List<ArticleFileEntity> findAllByArticleIdOrderByCreatedAtAsc(String articleId);

    void deleteByArticleId(String articleId);

}
