package org.chomookun.arch4j.core.verification.processor.notification;

import org.chomookun.arch4j.core.verification.processor.VerifierProcessor;
import org.chomookun.arch4j.core.verification.processor.VerifierProcessorDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class NotificationVerifierProcessorDefinition extends VerifierProcessorDefinition {

    @Override
    public String getType() {
        return "notification";
    }

    @Override
    public String getName() {
        return "Notification Verifier";
    }

    @Override
    public Class<? extends VerifierProcessor> getTypeClass() {
        return NotificationVerifierProcessor.class;
    }

    @Override
    public String configTemplate() {
        return "";
    }

}
