package org.chomookun.arch4j.core.verification.processor.totp;

import org.chomookun.arch4j.core.verification.processor.VerifierProcessor;
import org.chomookun.arch4j.core.verification.processor.VerifierProcessorDefinition;
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
