package org.chomookun.arch4j.core.notification.repository;

import org.chomookun.arch4j.core.notification.entity.NotificationEntity;
import org.chomookun.arch4j.core.notification.entity.NotificationEntity_;
import org.chomookun.arch4j.core.notification.model.NotificationSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity,String>, JpaSpecificationExecutor<NotificationEntity> {

    /**
     * Finds all by alarm search
     * @param alarmSearch alarm search
     * @param pageable pageable
     * @return page of alarm entities
     */
    default Page<NotificationEntity> findAll(NotificationSearch alarmSearch, Pageable pageable) {
        // specification
        Specification<NotificationEntity> specification = Specification.where(null);
        if(alarmSearch.getNotificationId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(NotificationEntity_.NOTIFICATION_ID), '%'+ alarmSearch.getNotificationId() + '%'));
        }
        if(alarmSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(NotificationEntity_.NAME), '%' + alarmSearch.getName() + '%'));
        }
        // sort
        Sort sort = pageable.getSort().and(Sort.by(NotificationEntity_.SYSTEM_REQUIRED).descending());
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        // find all
        return findAll(specification, finalPageable);
    }

}
