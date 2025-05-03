package org.chomookun.arch4j.core.verification.verifier;

import org.chomookun.arch4j.core.verification.model.IssueCodeRequest;
import org.chomookun.arch4j.core.verification.model.IssueCodeResponse;
import org.chomookun.arch4j.core.verification.model.VerifyCodeRequest;
import org.chomookun.arch4j.core.verification.model.VerifyCodeResponse;

public abstract class Verifier {

    public abstract String getType();

    public abstract IssueCodeResponse issueCode(IssueCodeRequest request);

    public abstract VerifyCodeResponse verifyCode(VerifyCodeRequest request);

}
