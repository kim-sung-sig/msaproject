package com.example.userservice.application.components;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;

import com.example.userservice.domain.entity.User.UserRole;
import com.example.userservice.domain.model.UserForSecurity;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {

    private static JwtEncoder jwtEncoder;
    private static JwtDecoder jwtDecoder;
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret-key}")
    private String originSecretKey;

    @PostConstruct
    public void init() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(
                originSecretKey.getBytes(StandardCharsets.UTF_8),
                MacAlgorithm.HS256.name() // "HmacSHA256"
        );
        NimbusJwtEncoder encoder = new NimbusJwtEncoder(new ImmutableSecret<>(secretKeySpec.getEncoded()));
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
        JwtUtil.jwtEncoder = encoder;
        JwtUtil.jwtDecoder = decoder;
    }

    /**
     * JWT 생성
     */
    public static String generateToken(UserForSecurity user, long second) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(second))
                .subject(user.username())
                .claim("id", user.id())
                .claim("uuid", user.uuid().toString())
                .claim("username", user.username())
                .claim("role", user.role().name())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     * Refresh Token 생성 (껍데기, UUID만 포함)
     */
    public static String generateRefreshToken(long second) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(second))
                .subject(UUID.randomUUID().toString()) // 랜덤 UUID
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public static Optional<String> getJwtFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .filter(bearerToken -> bearerToken.startsWith(BEARER_PREFIX))
                .map(bearerToken -> bearerToken.substring(7));
    }

    /**
     * JWT 검증
     */
    public static boolean validateToken(String token) {
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * JWT에서 사용자 정보 추출
     */
    public static Long getUserId(String token) {
        return (Long) jwtDecoder.decode(token).getClaim("id");
    }

    public static String getUsername(String token) {
        return (String) jwtDecoder.decode(token).getClaim("username");
    }

    public static UUID getUserUuid(String token) {
        return UUID.fromString((String) jwtDecoder.decode(token).getClaim("uuid"));
    }

    public static UserRole getUserRole(String token) {
        return UserRole.valueOf((String) jwtDecoder.decode(token).getClaim("role"));
    }
    
    public static JwtUserInfo getUserInfo(String token) {
        Long userId = getUserId(token);
        UUID uuid = getUserUuid(token);
        String username = getUsername(token);
        UserRole role = getUserRole(token);
        return new JwtUserInfo(userId, uuid, username, role);
    }

    public record JwtUserInfo(
        Long userId,
        UUID uuid,
        String username,
        UserRole role
    ) {}
}