package org.oopscraft.arch4j.core.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, String>, JpaSpecificationExecutor<ArticleEntity>, ArticleRepositoryCustom {

}
