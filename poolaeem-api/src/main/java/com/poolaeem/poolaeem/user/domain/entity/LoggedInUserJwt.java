package com.poolaeem.poolaeem.user.domain.entity;

import com.poolaeem.poolaeem.common.component.time.TimeComponent;
import com.poolaeem.poolaeem.common.component.uuid.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "logged_in_user_jwt")
@NoArgsConstructor
public class LoggedInUserJwt {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "token", length = 700)
    private String token;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "issued_at")
    private ZonedDateTime issuedAt;

    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    public LoggedInUserJwt(String userId, String token, String clientIp, String userAgent, ZonedDateTime issuedAt, ZonedDateTime expiresAt) {
        this.userId = userId;
        this.token = token;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    @PrePersist
    private void prePersist() {
        this.id = UUIDGenerator.generate();
        this.createdAt = TimeComponent.nowUtc();
    }
}
