package org.chomookun.arch4j.core.example.repository;

import org.chomookun.arch4j.core.example.entity.ExampleEntity;
import org.chomookun.arch4j.core.example.entity.ExampleEntity_;
import org.chomookun.arch4j.core.example.model.ExampleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Example Repository
 */
@Repository
public interface ExampleRepository extends JpaRepository<ExampleEntity, String>, JpaSpecificationExecutor<ExampleEntity>, ExampleRepositoryCustom {

    /**
     * Finds all by example search
     * @param exampleSearch example search
     * @param pageable pageable
     * @return page of examples
     */
    default Page<ExampleEntity> findAll(ExampleSearch exampleSearch, Pageable pageable) {
        // where
        Specification<ExampleEntity> specification = Specification.where(null);
        if (exampleSearch.getExampleId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(ExampleEntity_.EXAMPLE_ID), "%" + exampleSearch.getExampleId() + "%"));
        }
        if (exampleSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(ExampleEntity_.NAME), "%" + exampleSearch.getName() + "%"));
        }
        if (exampleSearch.getType() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(ExampleEntity_.TYPE), exampleSearch.getType()));
        }
        if (exampleSearch.getCode() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(ExampleEntity_.CODE), exampleSearch.getCode()));
        }
        // sort
        Sort sort = pageable.getSort()
                .and(Sort.by(Sort.Order.asc("exampleId")));
        // find
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return findAll(specification, finalPageable);
    }

}
