package org.chomookun.arch4j.core.board.repository;

import org.chomookun.arch4j.core.board.entity.ArticleCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleCommentEntity, ArticleCommentEntity.Pk> {

    List<ArticleCommentEntity> findAllByArticleIdOrderByCreatedAtAsc(String articleId);

    List<ArticleCommentEntity> findAllByArticleIdAndParentCommentId(String articleId, String parentCommentId);

}
