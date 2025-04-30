package org.chomookun.arch4j.core.alarm.repository;

import org.chomookun.arch4j.core.alarm.entity.AlarmEntity;
import org.chomookun.arch4j.core.alarm.entity.AlarmEntity_;
import org.chomookun.arch4j.core.alarm.model.AlarmSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<AlarmEntity,String>, JpaSpecificationExecutor<AlarmEntity> {

    /**
     * Finds all by alarm search
     * @param alarmSearch alarm search
     * @param pageable pageable
     * @return page of alarm entities
     */
    default Page<AlarmEntity> findAll(AlarmSearch alarmSearch, Pageable pageable) {
        // specification
        Specification<AlarmEntity> specification = Specification.where(null);
        if(alarmSearch.getAlarmId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(AlarmEntity_.ALARM_ID), '%'+ alarmSearch.getAlarmId() + '%'));
        }
        if(alarmSearch.getAlarmName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(AlarmEntity_.ALARM_NAME), '%' + alarmSearch.getAlarmName() + '%'));
        }
        // sort
        Sort sort = pageable.getSort().and(Sort.by(AlarmEntity_.SYSTEM_REQUIRED).descending());
        Pageable finalPageable = pageable.isUnpaged()
                ? Pageable.unpaged(sort)
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        // find all
        return findAll(specification, finalPageable);
    }

}
