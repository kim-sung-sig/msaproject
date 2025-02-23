package com.example.userservice.application.usecase;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.userservice.common.exception.BusinessException;
import com.example.userservice.domain.entity.PasswordHistory;
import com.example.userservice.domain.entity.User;
import com.example.userservice.domain.repository.history.PasswordHistoryRepository;
import com.example.userservice.domain.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSecurityUseCaseService {

    private final UserRepository userRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;

    private final PasswordEncoder passwordEncoder;

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
        List<PasswordHistory> passwordHistories = passwordHistoryRepository.findByUserOrderByCreatedAtDesc(null, PageRequest.of(0, 5));
        boolean isPasswordReused = passwordHistories.stream()
                .anyMatch(passwordHistory -> passwordEncoder.matches(newPassword, passwordHistory.getPassword()));
        if (isPasswordReused)
            throw new BusinessException("이전에 사용한 비밀번호는 사용할 수 없습니다.");

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 저장 시작
        User.builder().build().changePassword(encodedPassword);
    }

}
