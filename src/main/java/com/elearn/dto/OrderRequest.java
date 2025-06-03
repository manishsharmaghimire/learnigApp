package com.elearn.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private String orderId;
    private double amount;
    private String courseId;
    private String userId;
    private String userName;
    private  String address;
    private String esewaOrderId;
}
