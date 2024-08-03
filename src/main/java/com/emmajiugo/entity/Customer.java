package com.emmajiugo.entity;

import com.emmajiugo.utils.DateHelper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter @Setter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private String role;
    @Column(name = "gender")
    private String gender;
    @Column(name = "country")
    private String country;
    @Column(name = "last_3ds_date")
    private LocalDateTime last3DSDate;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Customer(String email, String password, String role, String gender, String country) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.gender = gender;
        this.country = country;
    }

    @PrePersist
    protected void init() {
        this.createdAt = DateHelper.getCurrentTimeInUtc();
    }

    @PreUpdate
    protected void update() {
        this.updatedAt = DateHelper.getCurrentTimeInUtc();
    }
}
