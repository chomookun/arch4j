package org.chomookun.arch4j.core.verification.verifier.notification;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.notification.NotificationMessageService;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.verification.model.Verification;
import org.chomookun.arch4j.core.verification.verifier.Verifier;
import org.chomookun.arch4j.core.verification.verifier.VerifierFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
@RequiredArgsConstructor
public class NotificationVerifierFactory extends VerifierFactory {

    private final NotificationService notificationService;

    private final NotificationMessageService notificationMessageService;

    @Override
    public Class<? extends Verifier> getTypeClass() {
        return NotificationVerifier.class;
    }

    @Override
    public NotificationVerifier getVerifier(Verification verification) {
        NotificationVerifier notificationVerifier = (NotificationVerifier) super.getVerifier(verification);
        notificationVerifier.setNotificationService(notificationService);
        notificationVerifier.setNotificationMessageService(notificationMessageService);
        return notificationVerifier;
    }

}
