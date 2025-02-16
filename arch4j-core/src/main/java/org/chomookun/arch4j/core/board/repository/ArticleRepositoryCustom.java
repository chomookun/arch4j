package org.chomookun.arch4j.core.board.repository;

import org.chomookun.arch4j.core.board.entity.ArticleEntity;
import org.chomookun.arch4j.core.board.model.ArticleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {

    Page<ArticleEntity> findAll(ArticleSearch articleSearch, Pageable pageable);

}
