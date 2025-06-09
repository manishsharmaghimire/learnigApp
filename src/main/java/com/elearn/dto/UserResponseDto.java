package com.elearn.dto;

import lombok.Data;
import lombok.Setter;

import java.util.Set;

@Data
public class UserResponseDto {
    private String userId;
    private String email;
    private String fullName;
    private String profilePath;
    private boolean emailVerified;
    private boolean smsVerified;
    private Set<String> roles;


}
