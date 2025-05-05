package org.chomookun.arch4j.core.verification.repository;

import org.chomookun.arch4j.core.verification.entity.VerificationEntity;
import org.chomookun.arch4j.core.verification.entity.VerificationEntity_;
import org.chomookun.arch4j.core.verification.model.VerificationSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationEntity, String>, JpaSpecificationExecutor<VerificationEntity> {

    default Page<VerificationEntity> findAll(VerificationSearch verificationSearch, Pageable pageable) {
        // where
        Specification<VerificationEntity> specification = Specification.where(null);
        if (verificationSearch.getVerificationId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(VerificationEntity_.VERIFICATION_ID), verificationSearch.getVerificationId() + '%'));
        }
        if (verificationSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(VerificationEntity_.NAME), verificationSearch.getName() + '%'));
        }
        // find all
        return findAll(specification, pageable);
    }

}
