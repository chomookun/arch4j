package org.chomookun.arch4j.web.api.v1.login.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginResponse {

    private String accessToken;

    private String refreshToken;

}
