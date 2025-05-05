package org.chomookun.arch4j.core.verification.verifier.notification;

import org.chomookun.arch4j.core.verification.model.Verification;
import org.chomookun.arch4j.core.verification.verifier.Verifier;
import org.chomookun.arch4j.core.verification.verifier.VerifierDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class NotificationVerifierDefinition extends VerifierDefinition {

    @Override
    public String getType() {
        return "notification";
    }

    @Override
    public String getName() {
        return "Notification Verifier";
    }

    @Override
    public Class<? extends Verifier> getTypeClass() {
        return NotificationVerifier.class;
    }

    @Override
    public String configTemplate() {
        return "";
    }

}
