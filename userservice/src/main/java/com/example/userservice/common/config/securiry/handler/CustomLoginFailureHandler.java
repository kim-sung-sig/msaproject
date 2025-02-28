package com.example.userservice.common.config.securiry.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.userservice.common.constants.ConstantsUtil;
import com.example.userservice.domain.entity.User;
import com.example.userservice.domain.entity.User.UserStatus;
import com.example.userservice.domain.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String message = exception.getMessage();
        log.debug("Login Fail Message : {}", message);
        String username = request.getParameter("username");

        if (exception instanceof BadCredentialsException)  // 1. 비밀번호 불일치
            message = BadCredentialsExceptionHandle(username);
        else if (exception instanceof LockedException)  // 2. 계정 잠김 isAccountNonLocked
            message = "LOCKED";
        else if (exception instanceof DisabledException)  // 3. 계정 사용 불가 isAccountNonExpired
            message = "DISABLED";
        else if (exception instanceof AccountExpiredException)  // 4. 계정 만료로 잠김 isAccountNonExpired
            message = "LOCKED";
        else if (exception instanceof CredentialsExpiredException)  // 5. 비밀번호 변경 필요 isCredentialsNonExpired (이건 나중에 판별)
            message = "LOCKED";
        else if (exception instanceof UsernameNotFoundException)  // 6. UserDetailsService에서 유저를 찾을 수 없음
            message = "USERNAME_NOT_FOUND";
        else if (exception instanceof InternalAuthenticationServiceException)  // 7. db 에러
            message = "INTERNAL_SERVER_ERROR";
        else
            message = "UNKNOWN_ERROR";

        log.debug("Login Fail Message Convert to : {}", message);
        returnMessage(response, message);
    }

    private String BadCredentialsExceptionHandle(String username) {
        String message;
        Optional<User> userOp = userRepository.findByUsername(username);

        if (!userOp.isPresent())
            return "USERNAME_NOT_FOUND";

        User user = userOp.get();
        UserStatus status = user.getStatus();
        if (status.equals(UserStatus.DELETED))
            return "DISABLED";

        if (status.equals(UserStatus.LOCKED))
            return "LOCKED";

        user.incrementLoginFailureCount();
        int failCount = user.getLoginFailCount();
        if (failCount >= 15) { // 15회 이상 실패시 계정 잠김
            userLockProcess(user);
            message = "LOCKED";
        } else {
            userRepository.save(user);
            message = "PASSWORD_NOT_MATCH";
        }

        return message;
    }

    private void userLockProcess(User user) {
        user.userLock();
        userRepository.save(user);
        log.debug("계정 잠김 처리 user : {}", user);
    }

    private void returnMessage(HttpServletResponse response, String message) throws IOException {
        Map<String, Object> returnData = Collections.singletonMap(ConstantsUtil.RETURN_MESSAGE, message);

        // 응답을 작성
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(returnData);

        try (PrintWriter writer = response.getWriter()) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            writer.write(jsonResponse);
            writer.flush();
        }
    }

}
