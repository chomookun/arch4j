package org.oopscraft.arch4j.core.alarm.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.BaseEntity;

import javax.persistence.*;

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

    @Column(name = "alarm_client_id", length = 32)
    private String alarmClientId;

    @Column(name = "alarm_client_config")
    @Lob
    private String alarmClientConfig;

}
