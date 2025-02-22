package com.example.userservice.application.components;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.userservice.common.exception.BusinessException;
import com.example.userservice.domain.entity.PasswordHistory;
import com.example.userservice.domain.entity.User;
import com.example.userservice.domain.repository.history.PasswordHistoryRepository;
import com.example.userservice.domain.repository.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserComponent {

    private final UserRepository userRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void passwordUpdateProcess(User user, String newPassword) {
        // 이전 비밀 번호와 동일 한지 확인
        List<PasswordHistory> passwordHistories = passwordHistoryRepository.findTop5ByUserOrderByCreatedAtDesc(user);
        boolean isPasswordReused = passwordHistories.stream()
                .anyMatch(passwordHistory -> passwordEncoder.matches(newPassword, passwordHistory.getPassword()));
        if (isPasswordReused)
            throw new BusinessException("비밀번호는 이전에 사용한 비밀번호와 동일할 수 없습니다.");

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 저장 시작
        user.changePassword(encodedPassword);
        userRepository.save(user);

        passwordHistoryRepository.save(PasswordHistory.builder().user(user).password(encodedPassword).build()); // passwordHistory 저장
    }

}
