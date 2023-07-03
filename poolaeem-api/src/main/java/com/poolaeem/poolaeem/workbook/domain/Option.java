package com.poolaeem.poolaeem.workbook.domain;

import com.poolaeem.poolaeem.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "option")
public class Option extends BaseEntity {
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
}
