package com.hexdump95.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiresAtMinutes:20}")
    private Long expiresAt;

    @Value("${jwt.issuer}")
    private String issuer;

    public String createToken(Authentication authentication) {
        return generateToken(authentication.getName(), authoritiesToRoles(authentication.getAuthorities()));
    }

    public String createToken(UUID userId) {
        return generateToken(userId.toString(), List.of(RolesConstants.USER));
    }

    private List<String> authoritiesToRoles(Collection<? extends GrantedAuthority> authorities) {
        return authorities
                .stream().map(auth -> auth.getAuthority().substring(5))
                .collect(Collectors.toList());
    }

    private String generateToken(String userId, List<String> roles){
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(userId)
                .withExpiresAt(new Date(new Date().getTime() + expiresAt * 60 * 1000))
                .withClaim("roles", roles)
                .sign(algorithm);
    }
}
