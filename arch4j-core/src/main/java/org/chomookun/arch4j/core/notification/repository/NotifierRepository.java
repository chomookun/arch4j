package org.chomookun.arch4j.core.notification.repository;

import org.chomookun.arch4j.core.notification.entity.NotifierEntity;
import org.chomookun.arch4j.core.notification.entity.NotifierEntity_;
import org.chomookun.arch4j.core.notification.model.NotifierSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifierRepository extends JpaRepository<NotifierEntity,String>, JpaSpecificationExecutor<NotifierEntity> {

    /**
     * Finds all by alarm search
     * @param alarmSearch alarm search
     * @param pageable pageable
     * @return page of alarm entities
     */
    default Page<NotifierEntity> findAll(NotifierSearch alarmSearch, Pageable pageable) {
        // specification
        Specification<NotifierEntity> specification = Specification.where(null);
        if(alarmSearch.getNotifierId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(NotifierEntity_.NOTIFIER_ID), '%'+ alarmSearch.getNotifierId() + '%'));
        }
        if(alarmSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(NotifierEntity_.NAME), '%' + alarmSearch.getName() + '%'));
        }
        // sort
        Sort sort = pageable.getSort().and(Sort.by(NotifierEntity_.SYSTEM_REQUIRED).descending());
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        // find all
        return findAll(specification, finalPageable);
    }

}
