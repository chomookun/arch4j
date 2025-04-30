package org.chomookun.arch4j.core.alarm.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AlarmMessageSearch {

    private String alarmId;

    private AlarmMessage.Status status;

}
