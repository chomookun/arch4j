package org.chomookun.arch4j.core.email.repository;

import org.chomookun.arch4j.core.email.entity.EmailEntity_;
import org.chomookun.arch4j.core.email.model.EmailSearch;
import org.chomookun.arch4j.core.email.entity.EmailEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity,String>, JpaSpecificationExecutor<EmailEntity> {

    /**
     * Finds all by email search
     * @param emailSearch email search
     * @param pageable pageable
     * @return page of email entities
     */
    default Page<EmailEntity> findAll(EmailSearch emailSearch, Pageable pageable) {
        // specification
        Specification<EmailEntity> specification = (root, query, criteriaBuilder) -> null;
        if(emailSearch.getEmailId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(EmailEntity_.EMAIL_ID), '%' + emailSearch.getEmailId() + '%'));
        }
        if(emailSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(EmailEntity_.NAME), '%' + emailSearch.getName() + '%'));
        }
        // sort
        Sort sort = pageable.getSort().and(Sort.by(EmailEntity_.SYSTEM_REQUIRED).descending());
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        // find all
        return findAll(specification, pageable);
    }

}
