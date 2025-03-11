package com.example.userservice.application.components;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.userservice.common.constants.ConstantsUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenComponent {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "refresh:"; // key : refreshToken (String), value : userId (Long)
    private static final String REFRESH_TOKEN_USER_PREFIX = "refresh-user:"; // key : userId (Long), value : refreshToken (String)

    public Optional<Long> getUserIdFromRefreshToken(String refreshToken) {
        String stored = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + refreshToken);
        if (StringUtils.hasText(stored)) {
            return Optional.ofNullable(Long.valueOf(stored));
        }
        return Optional.empty();
    }

    public Optional<String> getRefreshTokenByUserId(Long userId) {
        String stored = redisTemplate.opsForValue().get(REFRESH_TOKEN_USER_PREFIX + String.valueOf(userId));
        if (StringUtils.hasText(stored)) {
            return Optional.ofNullable(stored);
        }
        return Optional.empty();
    }

    public boolean existingRefreshToken(String refreshToken) {
        String stored = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + refreshToken);
        return StringUtils.hasText(stored);
    }

    public boolean existingRefreshToken(Long userId) {
        String stored = redisTemplate.opsForValue().get(REFRESH_TOKEN_USER_PREFIX + String.valueOf(userId));
        return StringUtils.hasText(stored);
    }

    public void saveRefreshToken(String refreshToken, Long userId) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + refreshToken, String.valueOf(userId), ConstantsUtil.REFRESH_TOKEN_TTL, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(REFRESH_TOKEN_USER_PREFIX + String.valueOf(userId), refreshToken, ConstantsUtil.REFRESH_TOKEN_TTL, TimeUnit.SECONDS);
    }

    public void deleteRefreshToken(String refreshToken) {
        Optional<Long> userIdOp = getUserIdFromRefreshToken(refreshToken);

        if (!userIdOp.isPresent()) return;

        Long userId = userIdOp.get();
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + refreshToken);
        redisTemplate.delete(REFRESH_TOKEN_USER_PREFIX + String.valueOf(userId));

        log.debug("Delete RefreshToken : {}", refreshToken);
        log.debug("Delete RefreshToken Target UserId : {}", userId);
    }

    public void deleteRefreshToken(Long userId) {
        Optional<String> refreshTokenOp = getRefreshTokenByUserId(userId);

        if (!refreshTokenOp.isPresent()) return;

        String refreshToken = refreshTokenOp.get();
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + refreshToken);
        redisTemplate.delete(REFRESH_TOKEN_USER_PREFIX + String.valueOf(userId));

        log.debug("Delete RefreshToken : {}", refreshToken);
        log.debug("Delete RefreshToken Target UserId : {}", userId);
    }

}
