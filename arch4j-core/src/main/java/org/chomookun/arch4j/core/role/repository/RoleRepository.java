package org.chomookun.arch4j.core.role.repository;

import org.chomookun.arch4j.core.role.entity.RoleEntity;
import org.chomookun.arch4j.core.role.entity.RoleEntity_;
import org.chomookun.arch4j.core.role.model.RoleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String>, JpaSpecificationExecutor<RoleEntity> {

    /**
     * finds all by role search
     * @param roleSearch role search
     * @param pageable pageable
     * @return page of role entities
     */
    default Page<RoleEntity> findAll(RoleSearch roleSearch, Pageable pageable) {
        // where
        Specification<RoleEntity> specification = Specification.where(null);
        if(roleSearch.getRoleId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(RoleEntity_.ROLE_ID), '%' + roleSearch.getRoleId() + '%'));
        }
        if(roleSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(RoleEntity_.NAME), '%' + roleSearch.getName() + '%'));
        }
        // sort
        Sort sort = pageable.getSort().and(Sort.by(RoleEntity_.SYSTEM_REQUIRED).descending());
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        // find all
        return findAll(specification, finalPageable);
    }

}
