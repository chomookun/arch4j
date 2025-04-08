package org.chomookun.arch4j.core.security.repository;

import org.chomookun.arch4j.core.security.entity.AuthorityEntity;
import org.chomookun.arch4j.core.security.entity.AuthorityEntity_;
import org.chomookun.arch4j.core.security.model.AuthoritySearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, String>, JpaSpecificationExecutor<AuthorityEntity> {

    /**
     * find all by authority search
     * @param authoritySearch authority search
     * @param pageable pageable
     * @return page of authority entities
     */
    default Page<AuthorityEntity> findAll(AuthoritySearch authoritySearch, Pageable pageable) {
        // where
        Specification<AuthorityEntity> specification = Specification.where(null);
        if(authoritySearch.getAuthorityId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(AuthorityEntity_.AUTHORITY_ID), '%' + authoritySearch.getAuthorityId() + '%'));
        }
        if(authoritySearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(AuthorityEntity_.NAME), '%' + authoritySearch.getName() + '%'));
        }
        // sort
        Sort sort = pageable.getSort()
                .and(Sort.by(AuthorityEntity_.SYSTEM_REQUIRED).descending())
                .and(Sort.by(AuthorityEntity_.AUTHORITY_ID).ascending());
        // find all
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return findAll(specification, finalPageable);
    }

}
