package com.elearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id", length = 100)
    private String userId;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(length = 500)
    private String about;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "email_verified")
    private boolean emailVarified;

    @Column(name = "sms_verified")
    private boolean smsVerified;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createAt;

    @Column(name = "profile_path", length = 255)
    private String profilePath;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
}
