package com.example.userservice.domain.repository.profile;

import java.util.Optional;

import com.example.userservice.domain.model.FilePath;

public interface UserProfilePictureRepositoryCustom {

    Optional<FilePath> findFilePathById(Long profileId);
}
