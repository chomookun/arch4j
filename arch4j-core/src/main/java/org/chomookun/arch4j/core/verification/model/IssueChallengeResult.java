package org.chomookun.arch4j.core.verification.model;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueChallengeResult {

    private String verificationId;

    private String code;

    private String notificationId;

}
