package org.chomookun.arch4j.core.verification.adaptor.notification;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.notification.NotificationMessageService;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.verification.model.Verifier;
import org.chomookun.arch4j.core.verification.adaptor.VerifierProcessor;
import org.chomookun.arch4j.core.verification.adaptor.VerifierProcessorFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
@RequiredArgsConstructor
public class NotificationVerifierProcessorFactory extends VerifierProcessorFactory {

    private final NotificationService notificationService;

    private final NotificationMessageService notificationMessageService;

    @Override
    public Class<? extends VerifierProcessor> getTypeClass() {
        return NotificationVerifierProcessor.class;
    }

    @Override
    public NotificationVerifierProcessor getObject(Verifier verifier) {
        NotificationVerifierProcessor notificationVerifier = (NotificationVerifierProcessor) super.getObject(verifier);
        notificationVerifier.setNotificationService(notificationService);
        notificationVerifier.setNotificationMessageService(notificationMessageService);
        return notificationVerifier;
    }

}
