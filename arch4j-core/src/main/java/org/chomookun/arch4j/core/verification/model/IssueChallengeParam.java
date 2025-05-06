package org.chomookun.arch4j.core.verification.model;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueChallengeParam {

    private String verifierId;

    private String principal;

    private String reason;

    private String userId;

}
