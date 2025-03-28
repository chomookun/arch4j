package org.chomookun.arch4j.core.code.repository;

import org.chomookun.arch4j.core.code.entity.CodeEntity_;
import org.chomookun.arch4j.core.code.model.CodeSearch;
import org.chomookun.arch4j.core.code.entity.CodeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository 
public interface CodeRepository extends JpaRepository<CodeEntity,String>, JpaSpecificationExecutor<CodeEntity> {

    /**
     * find all by code search
     * @param codeSearch code search
     * @param pageable pageable
     * @return page of code entities
     */
    default Page<CodeEntity> findAll(CodeSearch codeSearch, Pageable pageable) {
        // where
        Specification<CodeEntity> specification = Specification.where(null);
        if(codeSearch.getCodeId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(CodeEntity_.CODE_ID), '%' + codeSearch.getCodeId() + '%'));
        }
        if(codeSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(CodeEntity_.NAME), '%' + codeSearch.getName() + '%'));
        }
        // sort
        Sort sort = pageable.getSort()
                .and(Sort.by(CodeEntity_.SYSTEM_REQUIRED).descending())
                .and(Sort.by(CodeEntity_.CODE_ID).ascending());
        // find all
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return findAll(specification, finalPageable);
    }

}
