package org.chomookun.arch4j.core.verification.repository;

import jakarta.persistence.NamedEntityGraph;
import org.chomookun.arch4j.core.verification.entity.VerificationEntity;
import org.chomookun.arch4j.core.verification.entity.VerificationEntity_;
import org.chomookun.arch4j.core.verification.model.VerificationSearch;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationEntity, String>, JpaSpecificationExecutor<VerificationEntity> {

    @Override
    @EntityGraph(attributePaths = {"user", "notificationMessage"})
    Page<VerificationEntity> findAll(Specification<VerificationEntity> specification, Pageable pageable);

    /**
     * Find all verification entities with search criteria
     * @param verificationSearch verification search criteria
     * @param pageable pageable
     * @return page of verification entities
     */
    default Page<VerificationEntity> findAll(VerificationSearch verificationSearch, Pageable pageable) {
        // where
        Specification<VerificationEntity> specification = Specification.where(null);
        if (verificationSearch.getNotificationId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(VerificationEntity_.NOTIFICATION_ID), verificationSearch.getNotificationId() + '%'));
        }
        if (verificationSearch.getPrincipal() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(VerificationEntity_.PRINCIPAL), verificationSearch.getPrincipal() + '%'));
        }
        // find all
        return findAll(specification, pageable);
    }

}
