package com.poolaeem.poolaeem.question.domain.entity;

import com.poolaeem.poolaeem.common.component.uuid.UUIDGenerator;
import com.poolaeem.poolaeem.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "problem")
@NoArgsConstructor
public class Problem extends BaseEntity {
    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id")
    private Workbook workbook;

    @Column(name = "question")
    private String question;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ProblemType type;

    @Column(name = "timeout")
    private Integer timeout;

    @Column(name = "option_count")
    private int optionCount;

    @Column(name = "problem_order")
    private int order;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY)
    private List<ProblemOption> options = new ArrayList<>();

    public Problem(String id, String question, List<ProblemOption> options) {
        this.id = id;
        this.question = question;
        this.options = options;
    }

    public Problem(Workbook workbook, String question, ProblemType type, int optionCount, int order) {
        this.workbook = workbook;
        this.question = question;
        this.type = type;
        this.optionCount = optionCount;
        this.order = order;
    }

    public Problem(String id, Workbook workbook, String question) {
        this.id = id;
        this.workbook = workbook;
        this.question = question;
    }

    public Problem(String id, Workbook workbook, String question, ProblemType type) {
        this.id = id;
        this.workbook = workbook;
        this.question = question;
        this.type = type;
    }

    @PrePersist
    private void prePersist() {
        this.id = UUIDGenerator.generate();
        if (type == ProblemType.CHECKBOX) {
            this.timeout = timeout == null ? 30 : timeout;
        } else {
            this.timeout = timeout == null ? 0 : timeout;
        }
        this.isDeleted = false;
    }

    public void updateQuestion(String question) {
        this.question = question;
    }

    public void updateOptionCount(int size) {
        this.optionCount = size;
    }
}
