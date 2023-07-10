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

    @Column(name = "problem_order")
    private int order;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY)
    private List<ProblemOption> options = new ArrayList<>();

    public Problem(Workbook workbook, String question, int order) {
        this.workbook = workbook;
        this.question = question;
        this.order = order;
    }

    @PrePersist
    private void prePersist() {
        this.id = UUIDGenerator.generate();
        this.isDeleted = false;
    }
}
