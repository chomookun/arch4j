package org.chomookun.arch4j.core.verification.verifier.notification;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.chomookun.arch4j.core.notification.NotificationMessageService;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.NotificationMessage;
import org.chomookun.arch4j.core.verification.model.*;
import org.chomookun.arch4j.core.verification.repository.VerificationIssueRepository;
import org.chomookun.arch4j.core.verification.verifier.*;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Objects;
import java.util.Properties;

public class NotificationVerifier extends Verifier {

    @Setter
    private NotificationService notificationService;

    @Setter
    private NotificationMessageService notificationMessageService;

    protected NotificationVerifier(Properties config) {
        super(config);
    }

    @Override
    public void issueCode(VerificationIssue verificationIssue) {
        String notificationId = getConfig().getProperty("notificationId");
        Notification notification = notificationService.getNotification(notificationId).orElseThrow();
        String to = verificationIssue.getPrincipal();
        String subject = "Verification";
        String code = generateCode();
        String content = String.format("Verification code: %s", code);
        NotificationMessage notificationMessage = notificationMessageService.sendNotificationMessage(notification, to, subject, content, null);
        verificationIssue.setCode(code);
        verificationIssue.setNotificationMessageId(notificationMessage.getMessageId());
    }

    @Override
    public boolean verifyCode(VerificationIssue verificationIssue, String code) {
        if (Objects.equals(verificationIssue.getCode(), code)) {
            return true;
        }
        return false;
    }

}
