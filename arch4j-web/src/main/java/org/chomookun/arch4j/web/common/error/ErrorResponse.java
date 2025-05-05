package org.chomookun.arch4j.web.common.error;

import lombok.*;

import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    @Builder.Default
    private Instant timestamp = Instant.now();

    private Integer status;

    private String error;

    private String message;

    private String path;

    private boolean redirect;

    private String redirectUri;

}
