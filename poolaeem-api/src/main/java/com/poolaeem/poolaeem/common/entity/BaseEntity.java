package com.poolaeem.poolaeem.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class BaseEntity {

    @Column(name = "CREATED_BY")
    @CreatedBy
    private String createdBy;

    @Column(name = "UPDATED_BY")
    @LastModifiedBy
    private String updatedBy;

    @Column(name = "CREATED_AT")
    @CreatedDate
    private ZonedDateTime createdAt;

    @Column(name = "UPDATED_AT")
    @LastModifiedDate
    private ZonedDateTime updatedAt;
}
