package com.example.userservice.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userservice.entity.NickNameHistory;
import com.example.userservice.entity.PasswordHistory;
import com.example.userservice.entity.User;
import com.example.userservice.enums.UserRole;
import com.example.userservice.enums.UserStatus;
import com.example.userservice.repository.history.NickNameHistoryRepository;
import com.example.userservice.repository.history.PasswordHistoryRepository;
import com.example.userservice.repository.user.UserRepository;
import com.example.userservice.request.CreateUserRequest;
import com.example.userservice.request.UpdateUserRequest;
import com.example.userservice.util.CommonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NickNameHistoryRepository nickNameHistoryRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
    public void createUser(CreateUserRequest request) {
        // 변수 추출
        String username = request.getUsername().trim();
        if (!CommonUtil.isUsernameValid(username)) throw new RuntimeException("Username is not valid");
        String password = request.getPassword().trim();
        if (!CommonUtil.isPasswordValid(password)) throw new RuntimeException("Password is not valid");

        String name = request.getName().trim();
        if (!CommonUtil.isNameValid(name)) throw new RuntimeException("Name is not valid");
        String nickName = request.getNickName().trim();
        if (!CommonUtil.isNickNameValid(nickName)) throw new RuntimeException("NickName is not valid");
        String email = request.getEmail().trim();
        if (!CommonUtil.isEmailValid(email)) throw new RuntimeException("Email is not valid");

        if (userRepository.existsByUsername(username)) throw new RuntimeException("Username already exists");

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 저장 시작
        User newUser = new User();
        newUser.setUuid(getEnabledUserUuid().orElseThrow(() -> new RuntimeException("UUID generation failed"))); // 사용가능한 UUID 추출 => 예외시 fail count
        newUser.setUsername(username);
        newUser.setPassword(encodedPassword);
        newUser.setRole(UserRole.USER);
        newUser.setStatus(UserStatus.ENABLED);
        newUser.setLoginFailCount(0);
        newUser.setName(name);
        newUser.setNickName(nickName);
        newUser.setNickSeq(getNickSeq(nickName));   // nickSeq 추출
        newUser.setEmail(email);
        userRepository.save(newUser);
        passwordHistoryRepository.save(PasswordHistory.builder().user(newUser).password(encodedPassword).build()); // passwordHistory 저장

        // 메시지 전송
        rabbitTemplate.convertAndSend("user.exchange", "user.create", newUser); // TODO : userEntity -> UserCreateEvent
    }

    // 회원 정보 수정
    public void updateUser(UpdateUserRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 변수 추출

        // 수정 시작

        // 메시지 전송

    }

    // 비밀번호 변경
    public void updateUserPassword(String inputPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) throw new RuntimeException("Authentication not found");

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        passwordUpdateProcess(user, inputPassword);

        // 메시지 전송
        // rabbitTemplate.convertAndSend("user.exchange", "user.update", user); // TODO : userEntity -> UserUpdateEvent
    }

    /**
     * 비밀번호 변경 (로그인 없이)
     * @param token
     * @param inputPassword
     */
    public void updateUserPasswordWithoutLogin(String token, String inputPassword) {
        String username = CommonUtil.getLoginedUserName();
        if (username == null) throw new RuntimeException("Username not found");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        passwordUpdateProcess(user, inputPassword);
    }

    // 이메일 변경
    public void updateUserEmail() {

    }

    // 회원 탈퇴
    public void deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) throw new RuntimeException("Authentication not found");
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
    }

    // 잠금 풀기 (비밀 번호 실패 횟수 초과로 잠금)
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

    private Optional<UUID> getEnabledUserUuid() {
        for (int i = 0; i < 50; i++) {
            UUID uuid = UUID.randomUUID();
            if (!userRepository.existsByUuid(uuid)) return Optional.of(uuid);
        }
        return Optional.empty();
    }

    private Long getNickSeq(String nickName) {
        return nickNameHistoryRepository.findById(nickName)
            .map(exist -> {
                // 존재하면 +1 한 후 반환
                Long updatedNickSeq = exist.getSeq() + 1;
                exist.setSeq(updatedNickSeq);
                nickNameHistoryRepository.save(exist);  // nickSeq 업데이트
                return updatedNickSeq;
            })
            .orElseGet(() -> {
                // 존재하지 않으면 새로 저장하고 1을 반환
                NickNameHistory newNickNameHistory = new NickNameHistory();
                newNickNameHistory.setNickName(nickName);
                newNickNameHistory.setSeq(1L);
                nickNameHistoryRepository.save(newNickNameHistory);  // 새로운 nickNameHistory 저장
                return 1L;
            });
    }

    private void passwordUpdateProcess(User user, String newPassword) {
        // 이전 비밀 번호와 동일 한지 확인
        List<PasswordHistory> passwordHistories = passwordHistoryRepository.findTop5ByUserOrderByCreatedAtDesc(user);
        boolean isPasswordReused = passwordHistories.stream()
                .anyMatch(passwordHistory -> passwordEncoder.matches(newPassword, passwordHistory.getPassword()));
        if (isPasswordReused) throw new RuntimeException("Password is reused");

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 저장 시작
        user.setPassword(encodedPassword);
        userRepository.save(user);

        passwordHistoryRepository.save(PasswordHistory.builder().user(user).password(encodedPassword).build()); // passwordHistory 저장
    }
}
