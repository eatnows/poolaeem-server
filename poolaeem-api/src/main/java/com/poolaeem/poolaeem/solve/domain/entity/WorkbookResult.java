package com.poolaeem.poolaeem.solve.domain.entity;

import com.poolaeem.poolaeem.common.component.uuid.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "workbook_result")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class WorkbookResult {
    @Id
    private String id;

    @Column(name = "workbook_id", nullable = false)
    private String workbookId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "total_questions", nullable = false)
    private Integer totalQuestion;

    @Column(name = "correct_count", nullable = false)
    private Integer correctCount;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private ZonedDateTime createdAt;

    public WorkbookResult(String workbookId, String userId, String userName, Integer totalQuestion, Integer correctCount) {
        this.workbookId = workbookId;
        this.userId = userId;
        this.userName = userName;
        this.totalQuestion = totalQuestion;
        this.correctCount = correctCount;
    }

    @PrePersist
    private void prePersist() {
        this.id = UUIDGenerator.generate();
    }
}
