package org.chomookun.arch4j.core.variable.repository;

import org.chomookun.arch4j.core.variable.entity.VariableEntity;
import org.chomookun.arch4j.core.variable.entity.VariableEntity_;
import org.chomookun.arch4j.core.variable.model.VariableSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VariableRepository extends JpaRepository<VariableEntity, String>, JpaSpecificationExecutor<VariableEntity> {

    /**
     * Finds variables by variable search
     * @param variableSearch variable search
     * @param pageable pageable
     * @return page of variable entities
     */
    default Page<VariableEntity> findAll(VariableSearch variableSearch, Pageable pageable) {
        Specification<VariableEntity> specification = Specification.where(null);
        // where
        if(variableSearch.getVariableId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(VariableEntity_.VARIABLE_ID), '%' + variableSearch.getVariableId() + '%'));
        }
        if(variableSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(VariableEntity_.NAME), '%' + variableSearch.getName() + '%'));
        }
        // sort
        Sort sort = pageable.getSort()
                .and(Sort.by(VariableEntity_.SYSTEM_REQUIRED).descending())
                .and(Sort.by(VariableEntity_.VARIABLE_ID).ascending());
        // find all
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return findAll(specification, finalPageable);
    }

}
