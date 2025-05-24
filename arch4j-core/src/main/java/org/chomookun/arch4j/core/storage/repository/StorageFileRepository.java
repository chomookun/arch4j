package org.chomookun.arch4j.core.storage.repository;

import org.chomookun.arch4j.core.storage.entity.StorageFileEntity;
import org.chomookun.arch4j.core.storage.entity.StorageFileEntity_;
import org.chomookun.arch4j.core.storage.model.StorageFileSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageFileRepository extends JpaRepository<StorageFileEntity, String>, JpaSpecificationExecutor<StorageFileEntity> {

    /**
     * Finds all by storage file search
     * @param storageFileSearch storage file search
     * @param pageable pageable
     * @return page of storage files
     */
    default Page<StorageFileEntity> findAll(StorageFileSearch storageFileSearch, Pageable pageable) {
        Specification<StorageFileEntity> specification = Specification.where(null);
        if (storageFileSearch.getTargetType() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(StorageFileEntity_.targetType), "%" + storageFileSearch.getTargetType() + "%"));
        }
        if (storageFileSearch.getTargetId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(StorageFileEntity_.targetId), "%" + storageFileSearch.getTargetId() + "%"));
        }
        if (storageFileSearch.getFilename() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(StorageFileEntity_.filename), "%" + storageFileSearch.getFilename() + "%"));
        }
        Sort sort = pageable.getSort()
                .and(Sort.by(StorageFileEntity_.CREATED_AT).descending());
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return findAll(specification, finalPageable);
    }

    /**
     * Finds by target
     * @param targetType target type
     * @param targetId target id
     * @return list of storage file
     */
    default List<StorageFileEntity> findAllByTarget(String targetType, String targetId) {
        Specification<StorageFileEntity> specification = Specification.where(null);
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(StorageFileEntity_.targetType), targetType));
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(StorageFileEntity_.targetId), targetId));
        return findAll(specification);
    }

}
