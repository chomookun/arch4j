package org.chomookun.arch4j.core.board.repository;

import org.chomookun.arch4j.core.board.entity.ArticleVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleVoteRepository extends JpaRepository<ArticleVoteEntity, ArticleVoteEntity.Pk> {

    List<ArticleVoteEntity> findAllByArticleId(String articleId);

}
