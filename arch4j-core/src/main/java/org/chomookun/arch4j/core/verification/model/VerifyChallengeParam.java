package org.chomookun.arch4j.core.verification.model;

import com.beust.ah.A;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VerifyChallengeParam {

    private String verificationId;

    private String code;

}
