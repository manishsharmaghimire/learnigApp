package com.elearn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "esewa_order_id", nullable = false, length = 100)
    private String esewaOrderId;

    @Column(nullable = false)
    private double amount;

    @Column(name = "payment_status", length = 50)
    private String pmtStatus;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "user_uid", nullable = false, length = 100)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_ref_id", referencedColumnName = "user_id")
    private User user;


    @Lob
    @Column(name = "address", columnDefinition = "CLOB")
    private String address;
}
