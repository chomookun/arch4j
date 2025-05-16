package org.chomookun.arch4j.core.message.repository;

import org.chomookun.arch4j.core.message.entity.MessageEntity;
import org.chomookun.arch4j.core.message.entity.MessageEntity_;
import org.chomookun.arch4j.core.message.model.MessageSearch;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, String>, JpaSpecificationExecutor<MessageEntity> {

    /**
     * Finds message entities by message search
     * @param messageSearch message search
     * @param pageable pageable
     * @return page of message entities
     */
    default Page<MessageEntity> findAll(MessageSearch messageSearch, Pageable pageable) {
        Specification<MessageEntity> specification = Specification.where(null);
        // where
        if(messageSearch.getMessageId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(MessageEntity_.MESSAGE_ID), '%' + messageSearch.getMessageId() + '%'));
        }
        if(messageSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(MessageEntity_.NAME), '%' + messageSearch.getName() + '%'));
        }
        // sort
        Sort sort = pageable.getSort()
                .and(Sort.by(MessageEntity_.SYSTEM_REQUIRED).descending())
                .and(Sort.by(MessageEntity_.MESSAGE_ID).ascending());
        // find all
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return findAll(specification, finalPageable);
    }

}
