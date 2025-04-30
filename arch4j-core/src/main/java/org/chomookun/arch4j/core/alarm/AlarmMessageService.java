package org.chomookun.arch4j.core.alarm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.alarm.entity.AlarmMessageEntity;
import org.chomookun.arch4j.core.alarm.model.Alarm;
import org.chomookun.arch4j.core.alarm.model.AlarmMessage;
import org.chomookun.arch4j.core.alarm.model.AlarmMessageSearch;
import org.chomookun.arch4j.core.alarm.repository.AlarmMessageRepository;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Lazy(false)
@RequiredArgsConstructor
@Slf4j
public class AlarmMessageService {

    private final AlarmService alarmService;

    private final AlarmMessageConsumer alarmMessageConsumer;
    private final AlarmMessageRepository alarmMessageRepository;

    public void sendAlarmMessage(Alarm alarm, String subject, String content) {
        AlarmMessage alarmMessage = AlarmMessage.builder()
                .messageId(IdGenerator.uuid())
                .alarmId(alarm.getAlarmId())
                .alarmName(alarm.getAlarmName())
                .subject(subject)
                .content(content)
                .build();
        alarmMessageConsumer.addAlarmMessage(alarmMessage);
    }

    public void sendAlarmMessage(String alarmId, String subject, String content) {
        Alarm alarm = alarmService.getAlarm(alarmId).orElseThrow();
        sendAlarmMessage(alarm, subject, content);
    }

    public Page<AlarmMessage> getAlarmMessages(AlarmMessageSearch alarmMessageSearch, Pageable pageable) {
        Page<AlarmMessageEntity> alarmMessageEntityPage = alarmMessageRepository.findAll(alarmMessageSearch, pageable);
        List<AlarmMessage> alarmMessages = alarmMessageEntityPage.getContent().stream()
                .map(AlarmMessage::from)
                .toList();
        long total = alarmMessageEntityPage.getTotalElements();
        return new PageImpl<>(alarmMessages, pageable, total);
    }

}
