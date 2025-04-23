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

    default Page<StorageFileEntity> findAll(StorageFileSearch storageObjectSearch, Pageable pageable) {
        Specification<StorageFileEntity> specification = Specification.where(null);
        if (storageObjectSearch.getTargetType() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(StorageFileEntity_.targetType), "%" + storageObjectSearch.getTargetType() + "%"));
        }
        if (storageObjectSearch.getTargetId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(StorageFileEntity_.targetId), "%" + storageObjectSearch.getTargetId() + "%"));
        }
        if (storageObjectSearch.getFilename() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(StorageFileEntity_.filename), "%" + storageObjectSearch.getFilename() + "%"));
        }
        Sort sort = pageable.getSort()
                .and(Sort.by(StorageFileEntity_.CREATED_AT).descending());
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return findAll(specification, finalPageable);
    }

    default List<StorageFileEntity> findAllByTarget(String targetType, String targetId) {
        Specification<StorageFileEntity> specification = Specification.where(null);
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(StorageFileEntity_.targetType), targetType));
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(StorageFileEntity_.targetId), targetId));
        return findAll(specification);
    }

}
