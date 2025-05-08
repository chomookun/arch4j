package org.chomookun.arch4j.core.verification.client.totp;

import org.chomookun.arch4j.core.verification.client.VerifierClient;
import org.chomookun.arch4j.core.verification.client.VerifierClientDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

@Component
@Lazy(false)
public class TotpVerifierClientDefinition extends VerifierClientDefinition {

    @Override
    public String getClientType() {
        return "totp";
    }

    @Override
    public String getName() {
        return "TOTP Verifier";
    }

    @Override
    public Class<? extends VerifierClient> getClassType() {
        return TotpVerifierClient.class;
    }

    @Override
    public String getPropertiesTemplate() {
        return "";  // default is sufficient
    }

}
