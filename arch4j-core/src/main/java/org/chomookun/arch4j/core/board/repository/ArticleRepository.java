package org.chomookun.arch4j.core.board.repository;

import org.chomookun.arch4j.core.board.entity.ArticleEntity;
import org.chomookun.arch4j.core.board.entity.ArticleEntity_;
import org.chomookun.arch4j.core.board.model.ArticleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, String>, JpaSpecificationExecutor<ArticleEntity> {

    /**
     * Finds all by article search
     * @param articleSearch article search
     * @param pageable pageable
     * @return page of article
     */
    default Page<ArticleEntity> findAll(ArticleSearch articleSearch, Pageable pageable) {
        // where
        Specification<ArticleEntity> specification = Specification.where(null);
        if (articleSearch.getBoardId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("boardId"), articleSearch.getBoardId()));
        }
        if (articleSearch.getUserId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("userId"), articleSearch.getUserId()));
        }
        if (articleSearch.getTitle() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("title"), '%' + articleSearch.getTitle() + '%'));
        }
        if (articleSearch.getContent() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("content"), '%' + articleSearch.getContent() + '%'));
        }
        // sort
        Sort sort = pageable.getSort()
                .and(Sort.by(ArticleEntity_.CREATED_AT).descending());
        // find all
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return findAll(specification, finalPageable);
    }

}
