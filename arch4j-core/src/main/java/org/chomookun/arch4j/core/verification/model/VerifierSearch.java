package org.chomookun.arch4j.core.verification.model;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VerifierSearch {

    private String verifierId;

    private String name;

}
