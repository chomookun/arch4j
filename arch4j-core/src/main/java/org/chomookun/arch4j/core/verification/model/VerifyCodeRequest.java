package org.chomookun.arch4j.core.verification.model;

import lombok.*;
import org.chomookun.arch4j.core.verification.verifier.Verifier;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VerifyCodeRequest {

    private String verificationId;

    private String code;

}
