package com.poolaeem.poolaeem.question.domain.entity;

import com.poolaeem.poolaeem.common.component.uuid.UUIDGenerator;
import com.poolaeem.poolaeem.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "workbook")
@NoArgsConstructor
public class Workbook extends BaseEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "problem_count")
    private int problemCount;

    @Column(name = "solved_count")
    private int solvedCount;

    @Column(name = "theme")
    @Enumerated(EnumType.STRING)
    private WorkbookTheme theme;

    @Column(name = "workbook_order")
    private int order;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public Workbook(String id, String userId, String name, String description, int problemCount, int solvedCount, WorkbookTheme theme) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.problemCount = problemCount;
        this.solvedCount = solvedCount;
        this.theme = theme;
    }

    public Workbook(String userId, String name, String description, WorkbookTheme theme, int order) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.problemCount = 0;
        this.solvedCount = 0;
        this.theme = theme;
        this.order = order;
    }

    @PrePersist
    private void prePersist() {
        this.id = UUIDGenerator.generate();
        this.theme = theme == null ? WorkbookTheme.PINK : theme;
        this.order = order == 0 ? 1 : order;
        this.isDeleted = false;
    }

    public void updateInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
