package org.chomookun.arch4j.core.verification.processor.totp;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.security.TotpService;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.core.verification.model.Verifier;
import org.chomookun.arch4j.core.verification.processor.VerifierProcessor;
import org.chomookun.arch4j.core.verification.processor.VerifierProcessorFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
@RequiredArgsConstructor
public class TotpVerifierProcessorFactory extends VerifierProcessorFactory {

    private final UserService userService;

    private final TotpService totpService;

    @Override
    public Class<? extends VerifierProcessor> getTypeClass() {
        return TotpVerifierProcessor.class;
    }

    @Override
    public TotpVerifierProcessor getObject(Verifier verifier) {
        TotpVerifierProcessor totpVerifier = (TotpVerifierProcessor) super.getObject(verifier);
        totpVerifier.setUserService(userService);
        totpVerifier.setTotpService(totpService);
        return totpVerifier;
    }

}
