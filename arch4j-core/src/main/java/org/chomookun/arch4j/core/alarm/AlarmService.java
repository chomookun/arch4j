package org.chomookun.arch4j.core.alarm;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.alarm.client.AlarmClient;
import org.chomookun.arch4j.core.alarm.client.AlarmClientFactory;
import org.chomookun.arch4j.core.alarm.entity.AlarmEntity;
import org.chomookun.arch4j.core.alarm.repository.AlarmRepository;
import org.chomookun.arch4j.core.alarm.model.Alarm;
import org.chomookun.arch4j.core.alarm.model.AlarmSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlarmService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final AlarmRepository alarmRepository;

    /**
     * Saves alarm
     * @param alarm alarm
     * @return saved alarm
     */
    @Transactional
    public Alarm saveAlarm(Alarm alarm) {
        AlarmEntity alarmEntity = Optional.ofNullable(alarm.getAlarmId())
                .flatMap(alarmRepository::findById)
                .orElse(AlarmEntity.builder()
                        .alarmId(alarm.getAlarmId())
                        .build());
        alarmEntity.setSystemUpdatedAt(LocalDateTime.now());
        alarmEntity.setName(alarm.getName());
        alarmEntity.setAlarmClientId(alarm.getAlarmClientId());
        alarmEntity.setAlarmClientConfig(alarm.getAlarmClientConfig());
        // saves
        AlarmEntity savedAlarmEntity = alarmRepository.saveAndFlush(alarmEntity);
        entityManager.refresh(savedAlarmEntity);
        return Alarm.from(savedAlarmEntity);
    }

    /**
     * Gets alarm
     * @param alarmId alarm id
     * @return alarm
     */
    public Optional<Alarm> getAlarm(String alarmId) {
        return alarmRepository.findById(alarmId)
                .map(Alarm::from);
    }

    /**
     * Deletes alarm
     * @param alarmId alarm id
     */
    @Transactional
    public void deleteAlarm(String alarmId) {
        alarmRepository.deleteById(alarmId);
        alarmRepository.flush();
    }

    /**
     * Gets alarms
     * @param alarmSearch alarm search
     * @param pageable pageable
     * @return page of alarms
     */
    public Page<Alarm> getAlarms(AlarmSearch alarmSearch, Pageable pageable) {
        Page<AlarmEntity> page = alarmRepository.findAll(alarmSearch, pageable);
        List<Alarm> alarms = page.getContent().stream()
                .map(Alarm::from)
                .collect(Collectors.toList());
        return new PageImpl<>(alarms, pageable, page.getTotalElements());
    }

    /**
     * Sends alarm
     * @param alarm alarm
     * @param subject subject
     * @param content content
     */
    public void sendAlarm(Alarm alarm, String subject, String content) {
        AlarmClient alarmClient = AlarmClientFactory.getAlarmClient(alarm);
        alarmClient.sendMessage(subject, content);
    }

    /**
     * Sends alarm by alarm id
     * @param alarmId alarm id
     * @param subject subject
     * @param content content
     */
    public void sendAlarm(String alarmId, String subject, String content) {
        Alarm alarm = getAlarm(alarmId).orElseThrow();
        sendAlarm(alarm, subject, content);
    }

}
