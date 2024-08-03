package com.emmajiugo.entity;

import com.emmajiugo.utils.DateHelper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "rules")
@Getter @Setter
@NoArgsConstructor
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "meta", columnDefinition = "TEXT")
    private String meta;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void init() {
        this.createdAt = DateHelper.getCurrentTimeInUtc();
    }

    @PreUpdate
    protected void update() {
        this.updatedAt = DateHelper.getCurrentTimeInUtc();
    }
}
