package org.chomookun.arch4j.core.verification.processor.notification;

import lombok.Setter;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.notification.NotifierService;
import org.chomookun.arch4j.core.notification.model.Notifier;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.template.TemplateService;
import org.chomookun.arch4j.core.template.model.Template;
import org.chomookun.arch4j.core.verification.model.*;
import org.chomookun.arch4j.core.verification.processor.*;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class NotificationVerifierProcessor extends VerifierProcessor {

    @Setter
    private NotifierService notificationService;

    @Setter
    private NotificationService notificationMessageService;

    @Setter
    private TemplateService templateService;

    public NotificationVerifierProcessor(Properties config) {
        super(config);
    }

    @Override
    public IssueChallengeResult issueChallenge(IssueChallengeParam param, Verification verification) {
        String notifierId = getConfig().getProperty("notifierId");
        String templateId = getConfig().getProperty("templateId");
        String to = param.getPrincipal();
        String code = generateCode();
        // template
        Template template = templateService.getTemplate(templateId).orElseThrow();
        templateService.renderTemplate(template, Map.of(
            "code",code
        ));
        String subject = template.getSubject();
        String content = template.getContent();
        // send notification
        Notifier notification = notificationService.getNotifier(notifierId).orElseThrow();
        Notification notificationMessage = notificationMessageService.sendNotification(notification, to, subject, content, null);
        return IssueChallengeResult.builder()
                .notificationId(notificationMessage.getNotificationId())
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
