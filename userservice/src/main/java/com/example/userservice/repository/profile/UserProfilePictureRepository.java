package com.example.userservice.repository.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.userservice.entity.UserProfilePicture;

@Repository
public interface UserProfilePictureRepository extends JpaRepository<UserProfilePicture, Long> {

}
