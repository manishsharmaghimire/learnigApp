package com.elearn.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.management.relation.Role;
import java.util.*;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @Column(name = "profile_path", length = 255)
    private String profilePath;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Roles> roles = new HashSet<Roles>();


    public void assignRole(Roles role){
        this.roles.add(role);
        role.getUsers().add(this);

    }
    public void removeRole(Roles role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }



}
