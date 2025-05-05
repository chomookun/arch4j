package org.chomookun.arch4j.core.verification.repository;

import org.chomookun.arch4j.core.verification.entity.VerificationChallengeEntity;
import org.chomookun.arch4j.core.verification.entity.VerificationIssueEntity_;
import org.chomookun.arch4j.core.verification.model.VerificationIssueSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationIssueRepository extends JpaRepository<VerificationChallengeEntity, String>, JpaSpecificationExecutor<VerificationChallengeEntity> {

    @Override
    @EntityGraph(attributePaths = {"user", "notificationMessage"})
    Page<VerificationChallengeEntity> findAll(Specification<VerificationChallengeEntity> specification, Pageable pageable);

    default Page<VerificationChallengeEntity> findAll(VerificationIssueSearch verificationIssueSearch, Pageable pageable) {
        // where
        Specification<VerificationChallengeEntity> specification = Specification.where(null);
        if (verificationIssueSearch.getVerificationId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(VerificationIssueEntity_.VERIFICATION_ID), verificationIssueSearch.getVerificationId() + '%'));
        }
        if (verificationIssueSearch.getPrincipal() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(VerificationIssueEntity_.PRINCIPAL), verificationIssueSearch.getPrincipal() + '%'));
        }
        // find all
        return findAll(specification, pageable);
    }

}
