package com.example.userservice.domain.repository.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.userservice.domain.entity.NickNameHistory;

@Repository
public interface NickNameHistoryRepository extends JpaRepository<NickNameHistory, String> {

}
