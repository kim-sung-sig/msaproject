package com.example.userservice.service;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userservice.entity.UserEntity;
import com.example.userservice.enums.UserLock;
import com.example.userservice.enums.UserRole;
import com.example.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    private final PasswordEncoder passwordEncoder;

    public void createUser() {
        log.info("User create event");

        String username = "Test1";
        if (userRepository.existsByUsername(username)) throw new RuntimeException("Username already exists");

        String password = "password1!";
        String encodedPassword = passwordEncoder.encode(password);

        UUID uuid = UUID.randomUUID();
        while (!userRepository.existsByUuid(uuid)) {uuid = UUID.randomUUID();}

        UserEntity userEntity = new UserEntity(uuid, username, encodedPassword, UserRole.USER, null, null, null, null, null, UserLock.ENABLED);
        userRepository.save(userEntity);
        rabbitTemplate.convertAndSend("user.exchange", "user.create", userEntity); // TODO : userEntity -> UserCreateEvent
    }
}
