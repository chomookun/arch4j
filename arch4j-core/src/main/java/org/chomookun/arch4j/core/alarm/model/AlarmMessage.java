package org.chomookun.arch4j.core.alarm.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.chomookun.arch4j.core.alarm.entity.AlarmMessageEntity;

import java.time.Instant;

@Builder
@Getter
@Setter
public class AlarmMessage {

    private String messageId;

    private String alarmId;

    private String alarmName;

    private String subject;

    private String content;

    private Instant submittedAt;

    private Instant sentAt;

    private Status status;

    private String errorMessage;

    public enum Status {
        SUBMITTED,
        COMPLETED,
        FAILED
    }

    public static AlarmMessage from(AlarmMessageEntity alarmMessageEntity) {
        return AlarmMessage.builder()
                .messageId(alarmMessageEntity.getMessageId())
                .alarmId(alarmMessageEntity.getAlarmId())
                .alarmName(alarmMessageEntity.getAlarmName())
                .subject(alarmMessageEntity.getSubject())
                .content(alarmMessageEntity.getContent())
                .submittedAt(alarmMessageEntity.getSubmittedAt())
                .sentAt(alarmMessageEntity.getSentAt())
                .status(alarmMessageEntity.getStatus())
                .errorMessage(alarmMessageEntity.getErrorMessage())
                .build();
    }

}
