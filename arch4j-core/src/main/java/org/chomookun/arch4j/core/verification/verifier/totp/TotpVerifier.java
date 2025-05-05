package org.chomookun.arch4j.core.verification.verifier.totp;

import lombok.Setter;
import org.chomookun.arch4j.core.security.TotpService;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.verification.model.*;
import org.chomookun.arch4j.core.verification.verifier.*;

import java.util.Properties;

public class TotpVerifier extends Verifier {

    @Setter
    private UserService userService;

    @Setter
    private TotpService totpService;

    protected TotpVerifier(Properties config) {
        super(config);
    }

    @Override
    public void issueCode(VerificationIssue verificationIssue) {
        // void
    }

    @Override
    public boolean verifyCode(VerificationIssue verificationIssue, String code) {
        User user = userService.getUser(verificationIssue.getUserId()).orElseThrow();
        return totpService.verifyTotpCode(user.getTotpSecret(), code);
    }

}
