package org.chomookun.arch4j.core.verification.client.notification;

import org.chomookun.arch4j.core.verification.client.VerifierClient;
import org.chomookun.arch4j.core.verification.client.VerifierClientDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

@Component
@Lazy(false)
public class NotificationVerifierClientDefinition extends VerifierClientDefinition {

    @Override
    public String getClientType() {
        return "notification";
    }

    @Override
    public String getName() {
        return "Notification Verifier";
    }

    @Override
    public Class<? extends VerifierClient> getClassType() {
        return NotificationVerifierClient.class;
    }

    @Override
    public String getPropertiesTemplate() {
        StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add("notifierId=[Notifier ID]");
        stringJoiner.add("templateId=[Template ID]");
        return stringJoiner.toString();
    }

}
