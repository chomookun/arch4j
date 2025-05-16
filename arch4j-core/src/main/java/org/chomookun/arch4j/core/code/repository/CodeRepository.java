package org.chomookun.arch4j.core.code.repository;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.chomookun.arch4j.core.code.entity.*;
import org.chomookun.arch4j.core.code.model.CodeItemI18n;
import org.chomookun.arch4j.core.code.model.CodeSearch;
import org.chomookun.arch4j.core.common.i18n.I18nUtils;
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
        // exists subquery
        if(codeSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) -> {
                String locale = I18nUtils.getCurrentLocale();
                Subquery<Integer> subquery = query.subquery(Integer.class);
                Root<CodeI18nEntity> subRoot = subquery.from(CodeI18nEntity.class);
                subquery.select(criteriaBuilder.literal(1))
                        .where(
                                criteriaBuilder.equal(subRoot.get(CodeI18nEntity_.CODE_ID), root.get("codeId")),
                                criteriaBuilder.equal(subRoot.get(CodeI18nEntity_.LOCALE), locale),
                                criteriaBuilder.like(subRoot.get(CodeI18nEntity_.NAME), "%" + codeSearch.getName() + "%")
                        );
                return criteriaBuilder.exists(subquery);
            });
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
