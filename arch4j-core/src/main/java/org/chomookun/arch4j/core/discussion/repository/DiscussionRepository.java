package org.chomookun.arch4j.core.discussion.repository;

import org.chomookun.arch4j.core.discussion.entity.DiscussionEntity;
import org.chomookun.arch4j.core.discussion.entity.DiscussionEntity_;
import org.chomookun.arch4j.core.discussion.model.DiscussionSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscussionRepository extends JpaRepository<DiscussionEntity, String>, JpaSpecificationExecutor<DiscussionEntity> {

    default Page<DiscussionEntity> findAll(DiscussionSearch discussionSearch, Pageable pageable) {
        Specification<DiscussionEntity> specification = Specification.where(null);
        if (discussionSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(DiscussionEntity_.name), "%" + discussionSearch.getName() + "%"));
        }
        Sort sort = pageable.getSort().and(Sort.by(DiscussionEntity_.DISCUSSION_ID).descending());
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return findAll(specification, finalPageable);
    }

}
