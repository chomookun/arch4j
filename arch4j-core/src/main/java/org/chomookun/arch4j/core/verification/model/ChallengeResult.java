package org.chomookun.arch4j.core.verification.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChallengeResult {

    private String code;

    private String notificationMessageId;

}
