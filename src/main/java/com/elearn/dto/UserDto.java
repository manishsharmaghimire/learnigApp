package com.elearn.dto;


import com.elearn.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {


    private String userId;

    private String email;

    private String phoneNumber;

    private String password;

    private String about;

    private boolean isActive;

    private boolean emailVarified;

    private boolean smsVerified;

    private Date createAt;

    private String profilePath;

    private List<Order> orders = new ArrayList<>();
}
