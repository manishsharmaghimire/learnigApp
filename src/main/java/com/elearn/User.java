package com.elearn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class User {

    @Id
    private String userId;
    @Column(unique = true)
    private String email;
    private String phoneNumber;
    private String password;
    private String about;
    private boolean isActive;
    private boolean emailVarified;
    private boolean smsVerified;
    private Date createAt;
    private String profilePath;

}
