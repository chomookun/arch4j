package org.chomookun.arch4j.core.notification.repository;

import org.chomookun.arch4j.core.notification.entity.NotificationEntity;
import org.chomookun.arch4j.core.notification.entity.NotificationEntity_;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.NotificationSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, String>, JpaSpecificationExecutor<NotificationEntity> {

    /**
     * Updates result
     * @param notificationId alarm id
     * @param messageId message id
     * @param sentAt sent at
     * @param status status
     * @param errorMessage error message
     */
    @Transactional
    @Modifying
    @Query("""
            update  NotificationEntity ame
            set     ame.sentAt = :sentAt,
                    ame.status = :status,
                    ame.errorMessage = :errorMessage
            where   ame.notificationId = :messageId
            """)
    void updateResult(
            @Param("messageId") String messageId,
            @Param("sentAt") Instant sentAt,
            @Param("status") Notification.Status status,
            @Param("errorMessage") String errorMessage
    );

    /**
     * Finds all by alarm message search
     * @param notificationMessageSearch alarm message search
     * @param pageable pageable
     * @return page of alarm message entities
     */
    default Page<NotificationEntity> findAll(NotificationSearch notificationMessageSearch, Pageable pageable) {
        // where
        Specification<NotificationEntity> specification = Specification.where(null);
        if (notificationMessageSearch.getNotifierId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(NotificationEntity_.NOTIFIER_ID), notificationMessageSearch.getNotifierId()));
        }
        if (notificationMessageSearch.getStatus() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(NotificationEntity_.STATUS), notificationMessageSearch.getStatus()));
        }
        // sort
        Sort sort = pageable.getSort().and(Sort.by(NotificationEntity_.NOTIFICATION_ID).descending());
        // finds
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return findAll(specification, finalPageable);
    }

}
