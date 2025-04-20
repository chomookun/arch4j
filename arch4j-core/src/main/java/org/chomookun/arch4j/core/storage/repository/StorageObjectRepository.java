package org.chomookun.arch4j.core.storage.repository;

import org.chomookun.arch4j.core.storage.entity.StorageObjectEntity;
import org.chomookun.arch4j.core.storage.entity.StorageObjectEntity_;
import org.chomookun.arch4j.core.storage.model.StorageObjectSearch;
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
public interface StorageObjectRepository extends JpaRepository<StorageObjectEntity, String>, JpaSpecificationExecutor<StorageObjectEntity> {

    default Page<StorageObjectEntity> findAll(StorageObjectSearch storageFileSearch, Pageable pageable) {
        Specification<StorageObjectEntity> specification = Specification.where(null);
        if (storageFileSearch.getRefType() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(StorageObjectEntity_.refType), storageFileSearch.getRefType()));
        }
        if (storageFileSearch.getFilename() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(StorageObjectEntity_.filename), "%" + storageFileSearch.getFilename() + "%"));
        }
        Sort sort = pageable.getSort()
                .and(Sort.by(StorageObjectEntity_.LAST_MODIFIED).descending());
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return findAll(specification, finalPageable);
    }

    default List<StorageObjectEntity> findAllByRef(String refType, String refId) {
        Specification<StorageObjectEntity> specification = Specification.where(null);
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(StorageObjectEntity_.refType), refType));
        return findAll(specification);
    }

}
