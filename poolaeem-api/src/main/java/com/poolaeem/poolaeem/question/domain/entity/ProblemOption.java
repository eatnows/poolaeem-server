package com.poolaeem.poolaeem.question.domain.entity;

import com.poolaeem.poolaeem.common.component.uuid.UUIDGenerator;
import com.poolaeem.poolaeem.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "problem_option")
@NoArgsConstructor
public class ProblemOption extends BaseEntity {
    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Column(name = "value")
    private String value;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "option_order")
    private int order;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public ProblemOption(String id, String value, Boolean isCorrect) {
        this.id = id;
        this.value = value;
        this.isCorrect = isCorrect;
    }

    public ProblemOption(Problem problem, String value, Boolean isCorrect, int order) {
        this.problem = problem;
        this.value = value;
        this.isCorrect = isCorrect;
        this.order = order;
    }

    @PrePersist
    private void prePersist() {
        this.id = UUIDGenerator.generate();
        this.isDeleted = false;
    }
}
