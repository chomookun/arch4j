package org.chomookun.arch4j.core.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.security.model.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityTokenService {

    private final SecurityProperties securityProperties;

    /**
     * Encodes the security token
     * @param userDetails user details
     * @param expirationMinutes expiration minutes
     * @return security token
     */
    public String encodeSecurityToken(UserDetails userDetails, int expirationMinutes) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.claim("username", userDetails.getUsername());
        if(securityProperties.getSessionExpireMinutes() > 0) {
            jwtBuilder.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expirationMinutes).toInstant()));
        }
        jwtBuilder.signWith(SignatureAlgorithm.HS256, securityProperties.getSigningKey());
        jwtBuilder.compressWith(CompressionCodecs.GZIP);
        return jwtBuilder.compact();
    }

    /**
     * Decodes the security token
     * @param securityToken security token
     * @return user details
     */
    public UserDetails decodeSecurityToken(String securityToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(securityProperties.getSigningKey())
                .parseClaimsJws(securityToken).getBody();
        return UserDetailsImpl.builder()
                .username((String)claims.get("username"))
                .build();
    }

}
