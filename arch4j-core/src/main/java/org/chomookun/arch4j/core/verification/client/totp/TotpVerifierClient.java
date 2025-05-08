package org.chomookun.arch4j.core.verification.client.totp;

import lombok.Setter;
import org.chomookun.arch4j.core.security.TotpService;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.verification.model.*;
import org.chomookun.arch4j.core.verification.client.*;

import java.util.Properties;

public class TotpVerifierClient extends VerifierClient {

    @Setter
    private UserService userService;

    @Setter
    private TotpService totpService;

    public TotpVerifierClient(Properties config) {
        super(config);
    }

    @Override
    public IssueChallengeResult issueChallenge(IssueChallengeParam param, Verification verification) {
        String userId = verification.getPrincipal();
        User user = userService.getUser(userId).orElseThrow();
        return IssueChallengeResult.builder()
                .build();
    }

    @Override
    public VerifyChallengeResult verifyChallenge(VerifyChallengeParam param, Verification verification) {
        String userId = verification.getPrincipal();
        User user = userService.getUser(userId).orElseThrow();
        boolean totpResult = totpService.verifyTotpCode(user.getTotpSecret(), param.getCode());
        Verification.Result result = totpResult
                ? Verification.Result.SUCCESS
                : Verification.Result.INVALID_CODE;
        return VerifyChallengeResult.builder()
                .result(result)
                .build();
    }

}
