package org.chomookun.arch4j.core.verification.client.totp;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.security.TotpService;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.core.verification.model.Verifier;
import org.chomookun.arch4j.core.verification.client.VerifierClient;
import org.chomookun.arch4j.core.verification.client.VerifierClientFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
@RequiredArgsConstructor
public class TotpVerifierClientFactory extends VerifierClientFactory {

    private final UserService userService;

    private final TotpService totpService;

    @Override
    public Class<? extends VerifierClient> getTypeClass() {
        return TotpVerifierClient.class;
    }

    @Override
    public TotpVerifierClient getObject(Verifier verifier) {
        TotpVerifierClient totpVerifier = (TotpVerifierClient) super.getObject(verifier);
        totpVerifier.setUserService(userService);
        totpVerifier.setTotpService(totpService);
        return totpVerifier;
    }

}
