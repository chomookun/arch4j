package org.chomookun.arch4j.core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.security.model.SecurityToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityTokenService {

    private final SecurityProperties securityProperties;

    /**
     * Encodes the security token
     * @param securityToken security token
     * @param expirationMinutes expiration minutes
     * @return encoded security token string
     */
    public String encodeSecurityToken(SecurityToken securityToken, int expirationMinutes) {
        Instant expiresAt = ZonedDateTime.now().plusMinutes(expirationMinutes).toInstant();
        Algorithm sign = Algorithm.HMAC256(securityProperties.getSigningKey());
        return JWT.create()
                .withClaim("userId", securityToken.getUserId())
                .withExpiresAt(expiresAt)
                .sign(sign);
    }

    /**
     * Decodes the security token
     * @param encodedSecurityToken encoded security token
     * @return user details
     */
    public SecurityToken decodeSecurityToken(String encodedSecurityToken) {
        Algorithm sign = Algorithm.HMAC256(securityProperties.getSigningKey());
        JWTVerifier jwtVerifier = JWT
                .require(sign)
                .build();
        DecodedJWT decodedJwt = jwtVerifier.verify(encodedSecurityToken);
        String userId = decodedJwt.getClaim("userId").asString();
        return SecurityToken.builder()
                .userId(userId)
                .build();
    }

}
