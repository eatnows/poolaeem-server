package com.poolaeem.poolaeem.user.infra.repository;

import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {
    Optional<User> findByOauthProviderAndOauthIdAndIsDeletedFalse(OauthProvider provider, String oauthId);
    Optional<User> findByIdAndIsDeletedFalse(String userId);

    @Modifying
    @Query("update User u set u.lastLoggedAt = :loggedAt where u.id = :userId and u.isDeleted = false")
    void updateLastLoggedAtByIdAndIsDeletedFalse(String userId, ZonedDateTime loggedAt);
}
