package com.example.userservice.application.usecase;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.userservice.domain.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSecurityUseCaseService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 임시 비밀번호 발급하기
     * @param token
     * @param inputPassword
     */
    @Transactional
    public void issueTemporaryPassword(String email) {
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

    }

}
