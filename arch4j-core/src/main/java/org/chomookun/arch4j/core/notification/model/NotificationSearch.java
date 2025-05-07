package org.chomookun.arch4j.core.notification.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NotificationSearch {

    private String notifierId;

    private Notification.Status status;

}
