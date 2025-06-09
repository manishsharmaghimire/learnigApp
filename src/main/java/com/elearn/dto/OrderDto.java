package com.elearn.dto;

import com.elearn.entity.User;
import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    private String orderId;

    private String esewaOrderId;

    private double amount;

    @JsonProperty("pmtStatus")
    private String paymentStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date createdAt;
    private String userId;
    private String address;
}
