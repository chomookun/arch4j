package org.chomookun.arch4j.core.alarm.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.alarm.model.AlarmMessage;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.GenericEnumConverter;

import java.time.Instant;

@Entity
@Table(name = "core_alarm_message")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AlarmMessageEntity extends BaseEntity {

    @Id
    @Column(name = "message_id")
    private String messageId;

    @Column(name = "alarm_id")
    private String alarmId;

    @Column(name = "alarm_name")
    private String alarmName;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content", length = Integer.MAX_VALUE)
    @Lob
    private String content;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "status", length = 16)
    private AlarmMessage.Status status;

    @Column(name = "error_message", length = Integer.MAX_VALUE)
    @Lob
    private String errorMessage;

    @Converter(autoApply = true)
    public static class StatusConverter extends GenericEnumConverter<AlarmMessage.Status> { }

}
