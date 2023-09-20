package com.poolaeem.poolaeem.user.domain.entity;

import com.poolaeem.poolaeem.common.component.uuid.UUIDGenerator;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "logged_in_history")
@NoArgsConstructor
public class LoggedInHistory {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "logged_at")
    private ZonedDateTime loggedAt;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "user_agent")
    private String userAgent;

    public LoggedInHistory(String userId, ZonedDateTime loggedAt, String clientIp, String userAgent) {
        this.userId = userId;
        this.loggedAt = loggedAt;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }

    @PrePersist
    private void prePersist() {
        this.id = UUIDGenerator.generate();
    }
}
