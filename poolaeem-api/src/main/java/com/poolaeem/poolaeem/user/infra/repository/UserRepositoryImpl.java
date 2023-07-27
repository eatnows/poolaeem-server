package com.poolaeem.poolaeem.user.infra.repository;

import com.poolaeem.poolaeem.user.domain.entity.vo.QUserVo;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Optional;

import static com.poolaeem.poolaeem.user.domain.entity.QUser.user;


public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<UserVo> findDtoByUserIdAndIsDeleted(String userId, Boolean isDeleted) {
        return Optional.ofNullable(
                queryFactory
                        .select(new QUserVo(
                                Expressions.asString(userId),
                                user.email,
                                user.name,
                                user.role,
                                user.oauthProvider,
                                user.oauthId,
                                user.profileImage,
                                user.termsVersion,
                                user.isDeleted
                        )).from(user)
                        .where(user.id.eq(userId), eqIsDeleted(isDeleted))
                        .fetchFirst()
        );
    }

    private BooleanExpression eqIsDeleted(Boolean isDeleted) {
        return isDeleted == null ? null : user.isDeleted.eq(isDeleted);
    }
}
