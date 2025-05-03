package org.chomookun.arch4j.core.verification.model;

import lombok.*;
import org.chomookun.arch4j.core.verification.verifier.Verifier;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueCodeRequest {

    private String type;

    private String reason;

    private String notificationId;

    private String principal;

    private String userId;

}
