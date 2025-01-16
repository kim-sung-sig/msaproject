package com.example.userservice.service;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userservice.entity.User;
import com.example.userservice.enums.UserRole;
import com.example.userservice.enums.UserStatus;
import com.example.userservice.repository.history.NickNameHistoryRepository;
import com.example.userservice.repository.user.UserRepository;
import com.example.userservice.request.CreateUserRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NickNameHistoryRepository nickNameHistoryRepository;

    private final RabbitTemplate rabbitTemplate;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
    public void createUser(CreateUserRequest request) {
        // 변수 추출
        String username = request.getUsername().trim();
        String password = request.getPassword().trim();
        if (!isPasswordValid(password)) throw new RuntimeException("Password is not valid");

        String name = request.getName().trim();
        String nickName = request.getNickName().trim();
        String email = request.getEmail().trim();
        if (!isEmailValid(email)) throw new RuntimeException("Email is not valid");
        String phone = request.getPhone().trim();
        if (!isPhoneValid(phone)) throw new RuntimeException("Phone is not valid");

        // 유효성 검사
        if (userRepository.existsByUsername(username)) throw new RuntimeException("Username already exists");

        UUID uuid = UUID.randomUUID();
        int uuidCheckCount = 0;
        while (!userRepository.existsByUuid(uuid)) {
            if (uuidCheckCount > 100) throw new RuntimeException("UUID check failed");
            uuid = UUID.randomUUID();
        }

        User newUser = User.builder()
                .uuid(uuid)
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(UserRole.USER)
                .status(UserStatus.ENABLED)
                .loginFailCount(0)
                .name(name)
                .nickName(nickName)
                .nickSeq(null)
                .email(email)
                .phone(phone)
                .build();

        userRepository.save(newUser);
        rabbitTemplate.convertAndSend("user.exchange", "user.create", newUser); // TODO : userEntity -> UserCreateEvent
    }

    // 회원 정보 수정
    public void updateUser() {

    }

    // 회원 탈퇴
    public void deleteUser() {

    }

    // 잠금 풀기 (개인)
    public void unlockUser() {

    }

    // 회원 정보 조회
    public void getUser() {

    }

    // 회원 정보 조회 with Profile
    public void getUserWithProfile() {

    }

    // 회원 정보 상세 조회 (개인)
    public void getUserDetail() {

    }

    private boolean isPasswordValid(@NonNull String password) {
        return true;
    }

    private boolean isEmailValid(@NonNull String email) {
        return true;
    }

    private boolean isPhoneValid(@NonNull String phone) {
        return true;
    }

}
