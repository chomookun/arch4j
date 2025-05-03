package org.chomookun.arch4j.core.verification.verifier;

import org.chomookun.arch4j.core.verification.model.IssueCodeRequest;
import org.chomookun.arch4j.core.verification.model.IssueCodeResponse;
import org.chomookun.arch4j.core.verification.model.VerifyCodeRequest;
import org.chomookun.arch4j.core.verification.model.VerifyCodeResponse;

public class TotpVerifier extends Verifier {

    @Override
    public String getType() {
        return "totp";
    }

    @Override
    public IssueCodeResponse issueCode(IssueCodeRequest request) {
        return null;
    }

    @Override
    public VerifyCodeResponse verifyCode(VerifyCodeRequest request) {
        return null;
    }

}
