package com.example.userservice.application.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.userservice.api.request.user.CreateUserCommand;
import com.example.userservice.api.request.user.UpdateUserCommand;
import com.example.userservice.common.exception.BusinessException;
import com.example.userservice.common.exception.TemporaryException;
import com.example.userservice.common.util.CommonUtil;
import com.example.userservice.domain.entity.NickNameHistory;
import com.example.userservice.domain.entity.PasswordHistory;
import com.example.userservice.domain.entity.User;
import com.example.userservice.domain.entity.User.UserRole;
import com.example.userservice.domain.entity.User.UserStatus;
import com.example.userservice.domain.exception.UserNotFoundException;
import com.example.userservice.domain.repository.history.NickNameHistoryRepository;
import com.example.userservice.domain.repository.history.PasswordHistoryRepository;
import com.example.userservice.domain.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NickNameHistoryRepository nickNameHistoryRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;

    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    // 회원 가입
    @Transactional
    public void createUser(CreateUserCommand command) {
        // 변수 추출
        String username = command.username().trim();
        if (!CommonUtil.isUsernameValid(username))
            throw new BusinessException("Username is not valid");

        String password = command.password().trim();
        if (!CommonUtil.isPasswordValid(password))
            throw new BusinessException("유효한 비밀번호가 아닙니다. 비밀번호는 8자 이상 20자 이하, 영문 대소문자, 숫자, 특수문자를 포함해야 합니다.");

        String name = command.name().trim();
        if (!CommonUtil.isNameValid(name))
            throw new BusinessException("유효한 이름이 아닙니다.");

        String nickName = command.nickName().trim();
        if (!CommonUtil.isNickNameValid(nickName))
            throw new BusinessException("유효한 닉네임이 아닙니다. 닉네임은 2자 이상, 16자 이하로 입력해야 합니다.");

        String email = command.email().trim();
        if (!CommonUtil.isEmailValid(email))
            throw new BusinessException("유효한 이메일이 아닙니다.");

        if (userRepository.existsByUsername(username))
            throw new BusinessException("사용중인 아이디입니다.");

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 사용 가능한 UUID 추출
        UUID newUuid = getEnabledUserUuid()
                .orElseThrow(() -> new TemporaryException(5)); // 사용가능한 UUID 추출 예외시 failcount

        // nickName 중복 방지를 위한 nickSeq 추출
        long nickSeq = getNickSeq(nickName);

        // 사용자 정보 저장
        User newUser = User.builder()
                .uuid(newUuid)
                .username(username)
                .password(encodedPassword)
                .role(UserRole.USER)
                .status(UserStatus.ENABLED)
                .name(name)
                .nickName(nickName + nickSeq)
                .email(email)
                .build();
        userRepository.save(newUser);

        // passwordHistory 저장
        PasswordHistory passwordHistory = PasswordHistory.builder()
                .user(newUser)
                .password(encodedPassword)
                .build();
        passwordHistoryRepository.save(passwordHistory);
    }

    // 회원 정보 수정
    @Transactional
    public void updateUser(UpdateUserCommand request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException());

        // 변수 추출

        // 수정 시작
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new RuntimeException("Authentication not found");
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.userDelete();

        // 세션 무효화
        SecurityContextHolder.clearContext();
    }

    // 회원 정보 조회
    public void getUser() {

    }

    // 회원 정보 조회 with Profile
    public void getUserWithProfile() {

    }

    // 회원 정보 조회 (모든 프로필)
    public void getUserWithProfileAll() {

    }

    // 회원 정보 상세 조회 (개인)
    public void getUserDetail() {

    }

    private Optional<UUID> getEnabledUserUuid() {
        for (int i = 0; i < 50; i++) {
            UUID uuid = UUID.randomUUID();
            if (!userRepository.existsByUuid(uuid))
                return Optional.of(uuid);
        }
        return Optional.empty();
    }

    private Long getNickSeq(String nickName) {
        return nickNameHistoryRepository.findById(nickName)
                .map(exist -> {
                    // 존재하면 +1 한 후 반환
                    exist.increaseSeq();
                    nickNameHistoryRepository.save(exist); // nickSeq 업데이트
                    return exist.getSeq();
                })
                .orElseGet(() -> {
                    // 존재하지 않으면 새로 저장하고 1을 반환
                    NickNameHistory newNickNameHistory = NickNameHistory.builder()
                            .nickName(nickName)
                            .seq(1L)
                            .build();
                    nickNameHistoryRepository.save(newNickNameHistory); // 새로운 nickNameHistory 저장
                    return 1L;
                });
    }

}
