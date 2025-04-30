package org.chomookun.arch4j.core.alarm.repository;

import org.chomookun.arch4j.core.alarm.entity.AlarmMessageEntity;
import org.chomookun.arch4j.core.alarm.entity.AlarmMessageEntity_;
import org.chomookun.arch4j.core.alarm.model.AlarmMessage;
import org.chomookun.arch4j.core.alarm.model.AlarmMessageSearch;
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
public interface AlarmMessageRepository extends JpaRepository<AlarmMessageEntity, String>, JpaSpecificationExecutor<AlarmMessageEntity> {

    /**
     * Updates result
     * @param alarmId alarm id
     * @param messageId message id
     * @param sentAt sent at
     * @param status status
     * @param errorMessage error message
     */
    @Transactional
    @Modifying
    @Query("""
            update  AlarmMessageEntity ame
            set     ame.sentAt = :sentAt,
                    ame.status = :status,
                    ame.errorMessage = :errorMessage
            where   ame.alarmId = :alarmId
            and     ame.messageId = :messageId
            """)
    void updateResult(
            @Param("alarmId") String alarmId,
            @Param("messageId") String messageId,
            @Param("sentAt") Instant sentAt,
            @Param("status") AlarmMessage.Status status,
            @Param("errorMessage") String errorMessage
    );

    /**
     * Finds all by alarm message search
     * @param alarmMessageSearch alarm message search
     * @param pageable pageable
     * @return page of alarm message entities
     */
    default Page<AlarmMessageEntity> findAll(AlarmMessageSearch alarmMessageSearch, Pageable pageable) {
        // where
        Specification<AlarmMessageEntity> specification = Specification.where(null);
        if (alarmMessageSearch.getAlarmId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(AlarmMessageEntity_.ALARM_ID), alarmMessageSearch.getAlarmId()));
        }
        if (alarmMessageSearch.getStatus() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(AlarmMessageEntity_.STATUS), alarmMessageSearch.getStatus()));
        }
        // sort
        Sort sort = pageable.getSort().and(Sort.by(AlarmMessageEntity_.MESSAGE_ID).descending());
        // finds
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return findAll(specification, finalPageable);
    }

}
