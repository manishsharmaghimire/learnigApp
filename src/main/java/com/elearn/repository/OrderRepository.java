package com.elearn.repository;

import com.elearn.entity.Order;
import com.elearn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,String> {


    Optional<Order> findByEsewaOrderId(String pid);

}
