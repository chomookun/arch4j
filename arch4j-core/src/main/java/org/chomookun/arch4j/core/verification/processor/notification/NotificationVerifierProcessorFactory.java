package org.chomookun.arch4j.core.verification.processor.notification;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.notification.NotifierService;
import org.chomookun.arch4j.core.template.TemplateService;
import org.chomookun.arch4j.core.verification.model.Verifier;
import org.chomookun.arch4j.core.verification.processor.VerifierProcessor;
import org.chomookun.arch4j.core.verification.processor.VerifierProcessorFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
@RequiredArgsConstructor
public class NotificationVerifierProcessorFactory extends VerifierProcessorFactory {

    private final NotifierService notificationService;

    private final NotificationService notificationMessageService;

    private final TemplateService templateService;

    @Override
    public Class<? extends VerifierProcessor> getTypeClass() {
        return NotificationVerifierProcessor.class;
    }

    @Override
    public NotificationVerifierProcessor getObject(Verifier verifier) {
        NotificationVerifierProcessor notificationVerifier = (NotificationVerifierProcessor) super.getObject(verifier);
        notificationVerifier.setNotificationService(notificationService);
        notificationVerifier.setNotificationMessageService(notificationMessageService);
        notificationVerifier.setTemplateService(templateService);
        return notificationVerifier;
    }

}
