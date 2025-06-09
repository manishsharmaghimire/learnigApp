package com.elearn.dto;

import com.elearn.entity.Order;
import com.elearn.entity.Roles;
import com.elearn.validation.ValidEmail;
import com.elearn.validation.ValidPassword;
import com.elearn.validation.ValidPhoneNumber;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Data Transfer Object for User information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @ValidEmail
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 20, message = "Phone number must be between 10 and 20 characters")
    @Pattern(regexp = "^\\+?[0-9\\s\\(\\)\\-]{10,20}$", message = "Invalid phone number format")
    @ValidPhoneNumber
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @ValidPassword
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @Size(max = 500, message = "About cannot exceed 500 characters")
    private String about;

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private boolean emailVerified = false;

    @Builder.Default
    private boolean smsVerified = false;

    @PastOrPresent(message = "Create date cannot be in the future")
    @JsonProperty("createAt")
    private Date createdAt;

    @Size(max = 255, message = "Profile path must not exceed 255 characters")
    private String profilePath;

    @Valid
    @Builder.Default
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    @Valid
    private Set<Roles> roles;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty(access = Access.WRITE_ONLY)
    public void setPassword(String password) {
        this.password = password != null ? password.trim() : null;
    }
}
