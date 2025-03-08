package com.example.userservice.domain.repository.profile;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.userservice.common.enums.IsUsed;
import com.example.userservice.domain.entity.QUserProfilePicture;
import com.example.userservice.domain.model.FilePath;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserProfilePictureRepositoryCustomImpl implements UserProfilePictureRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<FilePath> findFilePathById(Long profileId) {
        QUserProfilePicture profile = QUserProfilePicture.userProfilePicture;

        FilePath result = queryFactory
            .select(Projections.constructor(FilePath.class,
                profile.filePath))
            .from(profile)
            .where(
                profile.id.eq(profileId)
                .and(profile.status.ne(IsUsed.DELETED)))
            .fetchOne();

        return Optional.ofNullable(result);
    }

}
