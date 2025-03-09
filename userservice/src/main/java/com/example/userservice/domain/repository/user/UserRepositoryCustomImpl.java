package com.example.userservice.domain.repository.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.userservice.domain.entity.QUser;
import com.example.userservice.domain.entity.User.UserStatus;
import com.example.userservice.domain.model.UserForSecurity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UserForSecurity> findByUsernameForSecurity(String username) {
        QUser user = QUser.user;

        UserForSecurity result = queryFactory
            .select(Projections.constructor(UserForSecurity.class,
                user.id
                , user.uuid
                , user.username
                , user.password
                , user.role
                , user.status
                , user.loginFailCount
                , user.lastLoginAt
                , user.createdAt))
            .from(user)
            .where(
                user.username.eq(username)
                .and(user.status.ne(UserStatus.DELETED)))
            .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<UserForSecurity> findByUserIdForSecurity(Long userId) {
        QUser user = QUser.user;

        UserForSecurity result = queryFactory
            .select(Projections.constructor(UserForSecurity.class,
                user.id
                , user.uuid
                , user.username
                , user.password
                , user.role
                , user.status
                , user.loginFailCount
                , user.lastLoginAt
                , user.createdAt))
            .from(user)
            .where(
                user.id.eq(userId)
                .and(user.status.ne(UserStatus.DELETED)))
            .fetchOne();

        return Optional.ofNullable(result);
    }
}
