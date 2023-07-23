package com.poolaeem.poolaeem.solve.domain.entity;

import com.poolaeem.poolaeem.common.component.uuid.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "answer_result")
@EntityListeners({AuditingEntityListener.class})
@NoArgsConstructor
public class AnswerResult {
    @Id
    private String id;

    @Column(name = "user_id")
    @CreatedBy
    private String userId;

    @Column(name = "problem_id")
    private String problemId;

    @Column(name = "answer")
    private String answer;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "created_at")
    @CreatedDate
    private ZonedDateTime createdAt;

    public AnswerResult(String problemId, String answer, Boolean isCorrect) {
        this.problemId = problemId;
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    public AnswerResult(String userId, String problemId, String answer, Boolean isCorrect) {
        this.userId = userId;
        this.problemId = problemId;
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    @PrePersist
    private void prePersist() {
        this.id = UUIDGenerator.generate();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
