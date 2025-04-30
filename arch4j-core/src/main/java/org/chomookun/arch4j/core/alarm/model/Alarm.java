package org.chomookun.arch4j.core.alarm.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.alarm.entity.AlarmEntity;
import org.chomookun.arch4j.core.common.data.BaseModel;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Alarm extends BaseModel {

    private String alarmId;

    private String alarmName;

    private String clientId;

    private String clientConfig;

    /**
     * Alarm factory method
     * @param alarmEntity alarm entity
     * @return alarm
     */
    public static Alarm from(AlarmEntity alarmEntity) {
        return Alarm.builder()
                .systemRequired(alarmEntity.isSystemRequired())
                .systemUpdatedAt(alarmEntity.getSystemUpdatedAt())
                .systemUpdatedBy(alarmEntity.getSystemUpdatedBy())
                .alarmId(alarmEntity.getAlarmId())
                .alarmName(alarmEntity.getAlarmName())
                .clientId(alarmEntity.getClientId())
                .clientConfig(alarmEntity.getClientConfig())
                .build();
    }

}
