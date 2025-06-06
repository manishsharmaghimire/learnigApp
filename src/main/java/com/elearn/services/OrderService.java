package com.elearn.services;

import com.elearn.dto.OrderDto;
import com.elearn.dto.OrderRequest;
import com.elearn.entity.Course;
import com.elearn.entity.User;

public interface OrderService {
     Course getCourse(OrderRequest orderRequest);
     User getUser(OrderRequest orderRequest);
     OrderDto createOrder(OrderRequest orderRequest);

     OrderDto verifyPayment(String paymentId, String orderId);

     String generatePaymentUrl(OrderDto orderDto);

     boolean verifyEsewaTransaction(String refId, String pid, String amt);

     OrderDto handleEsewaSuccessCallback(String refId, String pid, String amt);

     // Add this method if not already present
     OrderDto getOrderById(String orderId);
}
