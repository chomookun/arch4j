package org.chomookun.arch4j.core.comment.repository;

import org.chomookun.arch4j.core.comment.entity.CommentEntity;
import org.chomookun.arch4j.core.comment.entity.CommentEntity_;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String>, JpaSpecificationExecutor<CommentEntity> {

    default List<CommentEntity> findAllByThread(String thread) {
        // where
        Specification<CommentEntity> specification = Specification.where(null);
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(CommentEntity_.thread), thread));
        // sort
        Sort sort = Sort.by(CommentEntity_.CREATED_AT).ascending();
        return findAll(specification, sort);
    }

}
