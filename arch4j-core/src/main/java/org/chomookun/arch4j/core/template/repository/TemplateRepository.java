package org.chomookun.arch4j.core.template.repository;

import org.chomookun.arch4j.core.template.entity.TemplateEntity;
import org.chomookun.arch4j.core.template.entity.TemplateEntity_;
import org.chomookun.arch4j.core.template.model.TemplateSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<TemplateEntity,String>, JpaSpecificationExecutor<TemplateEntity> {

    default Page<TemplateEntity> findAll(TemplateSearch templateSearch, Pageable pageable) {
        // where
        Specification<TemplateEntity> specification = Specification.where(null);
        if(templateSearch.getTemplateId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(TemplateEntity_.TEMPLATE_ID), '%' + templateSearch.getTemplateId() + '%'));
        }
        if(templateSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(TemplateEntity_.NAME), '%' + templateSearch.getName() + '%'));
        }
        // sort
        Sort sort = pageable.getSort()
                .and(Sort.by(TemplateEntity_.SYSTEM_REQUIRED).descending());
        // find all
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return findAll(specification, finalPageable);
    }

}
