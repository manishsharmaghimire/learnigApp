package com.elearn.dto;

import com.elearn.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    private String esewaOrderId;

    private double amount;

    private String pmtStatus;

    private LocalDate createdDate;

    private String userId;

    private User user;

    private String address;
}

