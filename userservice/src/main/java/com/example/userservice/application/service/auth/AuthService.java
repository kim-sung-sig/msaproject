package com.example.userservice.application.service.auth;

import java.util.Optional;

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
import com.example.userservice.application.components.RefreshTokenComponent;
import com.example.userservice.common.constants.ConstantsUtil;
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
    private final RefreshTokenComponent refreshTokenComponent;
    private final PasswordHistoryRepository passwordHistoryRepository;

    private final PasswordEncoder passwordEncoder;

    /** 토큰 발급 (username, password)
     * @param loginRequest
     * @return
     */
    @Transactional
    public JwtTokenResponse createTokenByUsernameAndPassword(UserLoginRequest loginRequest) {
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

        refreshTokenComponent.deleteRefreshToken(user.id()); // 기존 리프래쉬 토큰이 있다면 삭제

        String accessToken = JwtUtil.generateToken(user, ConstantsUtil.ACCESS_TOKEN_TTL);
        String refreshToken = JwtUtil.generateRefreshToken(ConstantsUtil.REFRESH_TOKEN_TTL);
        refreshTokenComponent.saveRefreshToken(refreshToken, user.id());

        return new JwtTokenResponse(accessToken, refreshToken);
    }

    /** 토큰 발급 (refreshToken)
     * @param refreshToken
     * @return
     */
    /**
     * 토큰 발급 (refreshToken)
     * @param refreshToken
     * @return
     */
    @Transactional
    public JwtTokenResponse createTokenByRefreshToken(String refreshToken) {
        // 토큰이 존재하지 않으면 400에러
        if (!StringUtils.hasText(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token is missing");
        }

        if(!JwtUtil.validateToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }

        // 토큰이 저장소에 저장되어잇지 않으면 401? 에러
        Long userId = refreshTokenComponent.getUserIdFromRefreshToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        // 토큰과 매칭되는 유저조회 유저가 없으면 401?
        UserForSecurity user = userRepository.findByUserIdForSecurity(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        // 사용자가 잠긴 사용자 인지 확인
        if(Objects.equal(user.status(), UserStatus.LOCKED)) {
            throw new LockedException("User account is locked");
        }

        // 새로운 토큰 발급
        String newAccessToken = JwtUtil.generateToken(user, ConstantsUtil.ACCESS_TOKEN_TTL);
        String newRefreshToken = JwtUtil.generateRefreshToken(ConstantsUtil.REFRESH_TOKEN_TTL);
        refreshTokenComponent.saveRefreshToken(newRefreshToken, user.id()); // 저장소에 토큰저장

        return new JwtTokenResponse(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void oauthLogin() {

    }

    // 아이디 찾기
    @Transactional(readOnly = true)
    public String findUsername(String email) {
        return null;
    }

    /** 임시 비밀번호 발급하기
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

    // 임시 비밀번호 확인하기
    @Transactional(readOnly = true)
    public boolean verifyTemporaryPassword(String token) {
        return false;
    }

    // 임시 비밀번호 확인후 비밀번호 변경하기
    @Transactional
    public void changePasswordAfterVerification(String token, String newPassword) {

    }


}
