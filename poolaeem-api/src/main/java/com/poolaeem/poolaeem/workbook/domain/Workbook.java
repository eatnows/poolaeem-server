package com.poolaeem.poolaeem.workbook.domain;

import com.poolaeem.poolaeem.common.component.uuid.UUIDGenerator;
import com.poolaeem.poolaeem.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "workbook")
public class Workbook extends BaseEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "question_count")
    private int questionCount;

    @Column(name = "solved_count")
    private int solvedCount;

    @Column(name = "theme")
    @Enumerated(EnumType.STRING)
    private WorkbookTheme theme;

    @Column(name = "workbook_order")
    private int order;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @PrePersist
    private void prePersist() {
        this.id = UUIDGenerator.generate();
    }
}
