package org.chomookun.arch4j.core.verification.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VerificationSearch {

    private String notificationId;

    private String principal;

}
