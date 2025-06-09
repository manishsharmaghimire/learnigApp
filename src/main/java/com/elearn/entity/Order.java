package com.elearn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    private String orderId;

    @Column(name = "esewa_order_id", nullable = false, length = 100 ,unique = true)
    private String esewaOrderId;


    @Column(nullable = false)
    private double amount;

    @Column(name = "payment_status", length = 50)
    private String pmtStatus;

    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @PrePersist
    protected void onCreate() {
        createdDate = new Date();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_ref_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinTable(name = "course_id")
    private Course course;

    @Column(name = "address")
    private String address;
}
