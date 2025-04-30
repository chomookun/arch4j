package org.chomookun.arch4j.core.alarm.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.alarm.model.AlarmMessage;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;
import org.chomookun.arch4j.core.common.data.converter.GenericEnumConverter;

@Entity
@Table(name = "core_alarm")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AlarmEntity extends BaseEntity {

    @Id
    @Column(name = "alarm_id", length = 32)
    private String alarmId;

    @Column(name = "alarm_name")
    private String alarmName;

    @Column(name = "client_id", length = 32)
    private String clientId;

    @Column(name = "client_config", length = Integer.MAX_VALUE)
    @Lob
    private String clientConfig;

}
