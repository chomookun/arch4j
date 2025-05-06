package org.chomookun.arch4j.core.verification.adaptor.totp;

import org.chomookun.arch4j.core.verification.adaptor.VerifierProcessor;
import org.chomookun.arch4j.core.verification.adaptor.VerifierProcessorDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class TotpVerifierProcessorDefinition extends VerifierProcessorDefinition {

    @Override
    public String getType() {
        return "totp";
    }

    @Override
    public String getName() {
        return "TOTP Verifier";
    }

    @Override
    public Class<? extends VerifierProcessor> getTypeClass() {
        return TotpVerifierProcessor.class;
    }

    @Override
    public String configTemplate() {
        return "";
    }

}
