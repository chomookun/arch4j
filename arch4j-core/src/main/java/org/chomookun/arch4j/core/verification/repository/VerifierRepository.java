package org.chomookun.arch4j.core.verification.repository;

import org.chomookun.arch4j.core.verification.entity.VerifierEntity;
import org.chomookun.arch4j.core.verification.entity.VerifierEntity_;
import org.chomookun.arch4j.core.verification.model.VerifierSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifierRepository extends JpaRepository<VerifierEntity, String>, JpaSpecificationExecutor<VerifierEntity> {

    default Page<VerifierEntity> findAll(VerifierSearch verifierSearch, Pageable pageable) {
        // where
        Specification<VerifierEntity> specification = Specification.where(null);
        if (verifierSearch.getVerifierId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(VerifierEntity_.VERIFIER_ID), '%' + verifierSearch.getVerifierId() + '%'));
        }
        if (verifierSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(VerifierEntity_.NAME), '%' + verifierSearch.getName() + '%'));
        }
        // find all
        return findAll(specification, pageable);
    }

}
