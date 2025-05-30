package org.chomookun.arch4j.core.verification.client.notification;

import lombok.Setter;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.notification.NotifierService;
import org.chomookun.arch4j.core.notification.model.Notifier;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.template.TemplateService;
import org.chomookun.arch4j.core.template.model.Template;
import org.chomookun.arch4j.core.verification.model.*;
import org.chomookun.arch4j.core.verification.client.*;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class NotificationVerifierClient extends VerifierClient {

    @Setter
    private NotifierService notifierService;

    @Setter
    private NotificationService notificationService;

    @Setter
    private TemplateService templateService;

    public NotificationVerifierClient(Properties config) {
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
        Notifier notifier = notifierService.getNotifier(notifierId).orElseThrow();
        Notification notification = notificationService.sendNotification(notifier, subject, content, to, false);
        return IssueChallengeResult.builder()
                .code(code)
                .notificationId(notification.getNotificationId())
                .build();
    }

    @Override
    public VerifyChallengeResult verifyChallenge(VerifyChallengeParam param, Verification verification) {
        Verification.Result result;
        if (Objects.equals(param.getCode(), verification.getCode())) {
            result = Verification.Result.SUCCESS;
        } else {
            result = Verification.Result.INVALID_CODE;
        }
        return VerifyChallengeResult.builder()
                .result(result)
                .build();
    }

}
