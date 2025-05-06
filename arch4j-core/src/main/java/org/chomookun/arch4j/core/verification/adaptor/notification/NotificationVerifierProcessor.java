package org.chomookun.arch4j.core.verification.adaptor.notification;

import lombok.Setter;
import org.chomookun.arch4j.core.notification.NotificationMessageService;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.NotificationMessage;
import org.chomookun.arch4j.core.verification.model.*;
import org.chomookun.arch4j.core.verification.adaptor.*;

import java.util.Objects;
import java.util.Properties;

public class NotificationVerifierProcessor extends VerifierProcessor {

    @Setter
    private NotificationService notificationService;

    @Setter
    private NotificationMessageService notificationMessageService;

    public NotificationVerifierProcessor(Properties config) {
        super(config);
    }

    @Override
    public IssueChallengeResult issueChallenge(IssueChallengeParam param, Verification verification) {
        String notifierId = getConfig().getProperty("notifierId");
        Notification notification = notificationService.getNotification(notifierId).orElseThrow();
        String to = param.getPrincipal();
        String subject = "Verification";
        String code = generateCode();
        String content = String.format("Verification code: %s", code);
        NotificationMessage notificationMessage = notificationMessageService.sendNotificationMessage(notification, to, subject, content, null);
        return IssueChallengeResult.builder()
                .notificationId(notificationMessage.getMessageId())
                .code(code)
                .build();
    }

    @Override
    public VerifyChallengeResult verifyChallenge(VerifyChallengeParam param, Verification verification) {
        VerifyChallengeResult.Result result;
        if (Objects.equals(param.getCode(), verification.getCode())) {
            result = VerifyChallengeResult.Result.SUCCESS;
        } else {
            result = VerifyChallengeResult.Result.INVALID_CODE;
        }
        return VerifyChallengeResult.builder()
                .result(result)
                .build();
    }

}
