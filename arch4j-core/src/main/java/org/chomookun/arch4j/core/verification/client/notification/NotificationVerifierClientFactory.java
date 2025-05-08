package org.chomookun.arch4j.core.verification.client.notification;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.notification.NotifierService;
import org.chomookun.arch4j.core.template.TemplateService;
import org.chomookun.arch4j.core.verification.model.Verifier;
import org.chomookun.arch4j.core.verification.client.VerifierClient;
import org.chomookun.arch4j.core.verification.client.VerifierClientFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
@RequiredArgsConstructor
public class NotificationVerifierClientFactory extends VerifierClientFactory {

    private final NotifierService notificationService;

    private final NotificationService notificationMessageService;

    private final TemplateService templateService;

    @Override
    public Class<? extends VerifierClient> getTypeClass() {
        return NotificationVerifierClient.class;
    }

    @Override
    public NotificationVerifierClient getObject(Verifier verifier) {
        NotificationVerifierClient notificationVerifier = (NotificationVerifierClient) super.getObject(verifier);
        notificationVerifier.setNotifierService(notificationService);
        notificationVerifier.setNotificationService(notificationMessageService);
        notificationVerifier.setTemplateService(templateService);
        return notificationVerifier;
    }

}
