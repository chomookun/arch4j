package org.chomookun.arch4j.core.alarm.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AlarmSearch {

    private String alarmId;

    private String alarmName;

}
