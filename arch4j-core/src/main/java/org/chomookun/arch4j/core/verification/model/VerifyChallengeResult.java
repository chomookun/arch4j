package org.chomookun.arch4j.core.verification.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VerifyChallengeResult {

    private Result result;

    public enum Result {
        SUCCESS,
        INVALID_CODE,
        NOT_REQUESTED,
        ALREADY_VERIFIED,
        EXPIRED,
        TOO_MANY_TRIES,
    }

}
