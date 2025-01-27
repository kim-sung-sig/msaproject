package com.example.userservice.repository.history;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.userservice.entity.PasswordHistory;
import com.example.userservice.entity.User;

@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {

    /**
     * 이전 비밀번호 5개 조회
     * @param user
     * @return
     */
    List<PasswordHistory> findTop5ByUserOrderByCreatedAtDesc(User user);

}
