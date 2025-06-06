package com.elearn.dto;

import com.elearn.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    private String orderId;

    private String esewaOrderId;

    private double amount;

    private String pmtStatus;
    private LocalDate createdDate;
    private String userId;
    private String address;
}
