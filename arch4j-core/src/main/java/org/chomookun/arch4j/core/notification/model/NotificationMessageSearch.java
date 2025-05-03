package org.chomookun.arch4j.core.notification.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NotificationMessageSearch {

    private String notificationId;

    private NotificationMessage.Status status;

}
