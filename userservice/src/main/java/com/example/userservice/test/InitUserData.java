package com.example.userservice.test;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.userservice.entity.User;
import com.example.userservice.enums.UserRole;
import com.example.userservice.enums.UserStatus;
import com.example.userservice.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InitUserData {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Bean
    CommandLineRunner init() {
        return (args) -> {
            log.info("Initializing user data");
            userRepository.findByUsername("admin").ifPresentOrElse(
                user -> log.info("User data already exists"),
                () -> {
                    userRepository.save(User.builder()
                        .username("admin")
                        .uuid(UUID.randomUUID())
                        .password(passwordEncoder.encode("password1!"))
                        .role(UserRole.ADMIN)
                        .loginFailCount(0)
                        .status(UserStatus.ENABLED)
                        .name("Admin")
                        .nickName("Admin")
                        .nickSeq(1L)
                        .build());
                    log.info("User data initialized");
                });
        };
    }
}
