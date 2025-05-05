package org.chomookun.arch4j.core.verification.verifier.totp;

import org.chomookun.arch4j.core.verification.verifier.Verifier;
import org.chomookun.arch4j.core.verification.verifier.VerifierDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class TotpVerifierDefinition extends VerifierDefinition {

    @Override
    public String getType() {
        return "totp";
    }

    @Override
    public String getName() {
        return "TOTP Verifier";
    }

    @Override
    public Class<? extends Verifier> getTypeClass() {
        return TotpVerifier.class;
    }

    @Override
    public String configTemplate() {
        return "";
    }

}
