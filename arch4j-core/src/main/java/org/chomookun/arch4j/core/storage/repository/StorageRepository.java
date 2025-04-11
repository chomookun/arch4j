package org.chomookun.arch4j.core.storage.repository;

import org.chomookun.arch4j.core.storage.entity.StorageEntity;
import org.chomookun.arch4j.core.storage.entity.StorageEntity_;
import org.chomookun.arch4j.core.storage.model.StorageSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<StorageEntity, String>, JpaSpecificationExecutor<StorageEntity> {

    /**
     * Finds page of storage entities by storage search
     * @param storageSearch storage search
     * @param pageable pageable
     * @return page of storage entities
     */
    default Page<StorageEntity> findAll(StorageSearch storageSearch, Pageable pageable) {
        // specification
        Specification<StorageEntity> specification = Specification.where(null);;
        if (storageSearch.getStorageId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(StorageEntity_.STORAGE_ID), '%' + storageSearch.getStorageId() + '%'));
        }
        if (storageSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(StorageEntity_.NAME), '%' + storageSearch.getName() + '%'));
        }
        // sort
        Sort sort = pageable.getSort()
                .and(Sort.by(StorageEntity_.STORAGE_ID).ascending());
        // find all
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return findAll(specification, finalPageable);
    }

}
