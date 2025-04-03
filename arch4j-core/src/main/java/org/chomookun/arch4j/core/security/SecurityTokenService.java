package org.chomookun.arch4j.core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.security.model.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityTokenService {

    private final SecurityProperties securityProperties;

    private final UserDetailsService userDetailsService;

    /**
     * Encodes the security token
     * @param userDetails user details
     * @param expirationMinutes expiration minutes
     * @return security token
     */
    public String encodeSecurityToken(UserDetails userDetails, int expirationMinutes) {
        Instant expiresAt = ZonedDateTime.now().plusMinutes(expirationMinutes).toInstant();
        Algorithm sign = Algorithm.HMAC256(securityProperties.getSigningKey());
        return JWT.create()
                .withClaim("username", userDetails.getUsername())
                .withExpiresAt(expiresAt)
                .sign(sign);
    }

    /**
     * Decodes the security token
     * @param securityToken security token
     * @return user details
     */
    public UserDetails decodeSecurityToken(String securityToken) {
        Algorithm sign = Algorithm.HMAC256(securityProperties.getSigningKey());
        JWTVerifier jwtVerifier = JWT
                .require(sign)
                .build();
        DecodedJWT decodedJwt = jwtVerifier.verify(securityToken);
        String username = decodedJwt.getClaim("username").asString();
        return userDetailsService.loadUserByUsername(username);
    }

}
