package com.poolaeem.poolaeem.user.domain.entity;

import com.poolaeem.poolaeem.common.component.uuid.UUIDGenerator;
import com.poolaeem.poolaeem.common.encrypto.TextEncryptConverter;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "user")
@EntityListeners({AuditingEntityListener.class})
public class User {

    @Id
    @Column(name = "id", nullable = false, length = 32)
    private String id;

    @Convert(converter = TextEncryptConverter.class)
    @Column(name = "email", nullable = false, length = 200)
    private String email;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "oauth_provider", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private OauthProvider oauthProvider;

    @Column(name = "oauth_id", nullable = false, length = 300)
    private String oauthId;

    @Column(name = "profile_image", nullable = true, length = 32)
    private String profileImage;

    @Column(name = "terms_version", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private TermsVersion termsVersion;

    @Column(name = "is_deleted", nullable = false, length = 1)
    private Boolean isDeleted;

    @Column(name = "updated_by", nullable = false, length = 32)
    @LastModifiedBy
    private String updatedBy;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private ZonedDateTime updatedAt;

    public User() {
    }

    public User(String email, String name, OauthProvider oauthProvider, String oauthId, String profileImage, TermsVersion termsVersion) {
        this.email = email;
        this.name = name;
        this.oauthProvider = oauthProvider;
        this.oauthId = oauthId;
        this.profileImage = profileImage;
        this.termsVersion = termsVersion;
        this.role = UserRole.ROLE_USER;
    }

    @PrePersist
    private void prePersist() {
        id = UUIDGenerator.generateV4();
        isDeleted = false;
        updatedBy = id;
        createdAt = ZonedDateTime.now(ZoneId.of(ZoneOffset.UTC.getId()));
        updatedAt = ZonedDateTime.now(ZoneId.of(ZoneOffset.UTC.getId()));
        termsVersion = termsVersion == null ? TermsVersion.V1 : termsVersion;
    }
}
