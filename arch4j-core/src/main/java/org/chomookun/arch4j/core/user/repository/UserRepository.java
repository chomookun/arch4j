package org.chomookun.arch4j.core.user.repository;

import org.chomookun.arch4j.core.role.entity.RoleEntity;
import org.chomookun.arch4j.core.role.entity.RoleEntity_;
import org.chomookun.arch4j.core.user.entity.UserEntity;
import org.chomookun.arch4j.core.user.entity.UserEntity_;
import org.chomookun.arch4j.core.user.model.UserSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    /**
     * Finds user by username (using by spring security)
     * @param username username
     * @return user entity
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Finds users by user search
     * @param userSearch user search
     * @param pageable pageable
     * @return page of users
     */
    default Page<UserEntity> findAll(UserSearch userSearch, Pageable pageable) {
        // where
        Specification<UserEntity> specification = Specification.where(null);
        if (userSearch.getUsername() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(UserEntity_.USERNAME), '%' + userSearch.getUsername() + '%'));
        }
        if (userSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(UserEntity_.NAME), '%' + userSearch.getName() + '%'));
        }
        if (userSearch.getStatus() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(UserEntity_.STATUS), userSearch.getStatus()));
        }
        if (userSearch.getAdmin() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(UserEntity_.ADMIN), userSearch.getAdmin()));
        }
        // sort
        Sort sort = pageable.getSort().and(Sort.by(UserEntity_.ADMIN).descending());
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        // find all
        return findAll(specification, finalPageable);
    }

}
