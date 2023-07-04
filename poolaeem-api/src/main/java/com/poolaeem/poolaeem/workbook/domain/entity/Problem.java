package com.poolaeem.poolaeem.workbook.domain.entity;

import com.poolaeem.poolaeem.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "problem")
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
}
