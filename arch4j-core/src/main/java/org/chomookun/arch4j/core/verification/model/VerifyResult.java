package org.chomookun.arch4j.core.verification.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VerifyResult {

    private Result result;

    public enum Result {
        NOT_REQUESTED,
        ALREADY_VERIFIED,
        EXPIRED,
        TOO_MANY_TRIES,
        INVALID_CODE,
        SUCCESS

    }

}
