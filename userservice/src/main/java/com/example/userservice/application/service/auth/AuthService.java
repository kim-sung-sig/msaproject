package com.example.userservice.application.service.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.example.userservice.api.auth.request.UserLoginRequest;
import com.example.userservice.application.components.JwtUtil;
import com.example.userservice.application.components.RefreshTokenService;
import com.example.userservice.common.constants.ConstantsUtil;
import com.example.userservice.common.exception.BusinessException;
import com.example.userservice.domain.entity.PasswordHistory;
import com.example.userservice.domain.entity.User.UserStatus;
import com.example.userservice.domain.model.UserForSecurity;
import com.example.userservice.domain.repository.history.PasswordHistoryRepository;
import com.example.userservice.domain.repository.user.UserRepository;
import com.google.common.base.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordHistoryRepository passwordHistoryRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인 프로세스
     * 1. accessToken 발급
     * 2. refreshToken 발급 (userId 1개당 1개의 refreshToken 유지)
     * @param loginRequest
     */
    @Transactional
    public JwtTokenResponse login(UserLoginRequest loginRequest) {
        Optional<UserForSecurity> userOp = userRepository.findByUsernameForSecurity(loginRequest.username());

        // username에 일치하는 사용자가 존재하는지 확인
        if (!userOp.isPresent()) {
            throw new BadCredentialsException("Invalid username or password");
        }

        UserForSecurity user = userOp.get();
        // 사용자가 잠긴 사용자 인지 확인
        if(Objects.equal(user.status(), UserStatus.LOCKED)) {
            throw new LockedException("User account is locked");
        }

        // 사용자의 비밀번호 일치여부 확인
        if (!passwordEncoder.matches(loginRequest.password(), user.password())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        refreshTokenService.deleteRefreshToken(user.id()); // 기존 리프래쉬 토큰이 있다면 삭제

        String accessToken = JwtUtil.generateToken(user, ConstantsUtil.ACCESS_TOKEN_TTL);
        String refreshToken = JwtUtil.generateRefreshToken(ConstantsUtil.REFRESH_TOKEN_TTL);
        refreshTokenService.saveRefreshToken(refreshToken, user.id());

        return new JwtTokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public void oauthLogin() {

    }

    @Transactional
    public JwtTokenResponse tokenRefresh(String refreshToken) {

        if (!StringUtils.hasText(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is missing");
        }

        Optional<Long> userIdOp = refreshTokenService.getUserIdFromRefreshToken(refreshToken);
        if (!userIdOp.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        Long userId = userIdOp.get();
        Optional<UserForSecurity> userOp = userRepository.findByUserIdForSecurity(userId);
        if (!userOp.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        UserForSecurity user = userOp.get();
        String newAccessToken = JwtUtil.generateToken(user, ConstantsUtil.ACCESS_TOKEN_TTL);
        String newRefreshToken = JwtUtil.generateRefreshToken(ConstantsUtil.REFRESH_TOKEN_TTL);
        refreshTokenService.saveRefreshToken(newRefreshToken, user.id());

        return new JwtTokenResponse(newAccessToken, newRefreshToken);
    }

    // 아이디 찾기
    @Transactional(readOnly = true)
    public String findUsername(String email) {
        return null;
    }

    /**
     * 임시 비밀번호 발급하기
     * @param token
     * @param inputPassword
     */
    @Transactional
    public void issueTemporaryPassword(String username, String email) {
        // 1. 이메일로 사용자 찾기
        // 2. 임시 비밀번호 생성
        // 3. 임시 비밀번호 암호화
        // 4. 임시 비밀번호 저장
        // 5. 이메일로 임시 비밀번호 전송
    }

    /**
     * 임시 비밀번호 확인하기
     */
    @Transactional(readOnly = true)
    public boolean verifyTemporaryPassword(String token) {
        return false;
    }

    // 임시 비밀번호 확인후 비밀번호 변경하기
    @Transactional
    public void changePasswordAfterVerification(String token, String newPassword) {
        // 1. 임시 비밀번호 확인
        // 2. 비밀번호 암호화
        // 3. 비밀번호 변경

        // 이전 비밀 번호와 동일 한지 확인
        List<PasswordHistory> passwordHistories = passwordHistoryRepository.findByUserIdOrderByCreatedAtDesc(null, PageRequest.of(0, 5));
        boolean isPasswordReused = passwordHistories.stream()
                .anyMatch(passwordHistory -> passwordEncoder.matches(newPassword, passwordHistory.getPassword()));
        if (isPasswordReused)
            throw new BusinessException("이전에 사용한 비밀번호는 사용할 수 없습니다.");

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 저장 시작
    }


}
