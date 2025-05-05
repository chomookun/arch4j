package org.chomookun.arch4j.core.verification.verifier.totp;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.security.TotpService;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.core.verification.model.Verification;
import org.chomookun.arch4j.core.verification.verifier.Verifier;
import org.chomookun.arch4j.core.verification.verifier.VerifierFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
@RequiredArgsConstructor
public class TotpVerifierFactory extends VerifierFactory {

    private final UserService userService;

    private final TotpService totpService;

    @Override
    public Class<? extends Verifier> getTypeClass() {
        return TotpVerifier.class;
    }

    @Override
    public TotpVerifier getVerifier(Verification verification) {
        TotpVerifier totpVerifier = (TotpVerifier) super.getVerifier(verification);
        totpVerifier.setUserService(userService);
        totpVerifier.setTotpService(totpService);
        return totpVerifier;
    }

}
