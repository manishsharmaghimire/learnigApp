package com.elearn.repository;

import com.elearn.entity.Order;
import com.elearn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,String> {
}
