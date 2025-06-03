package com.elearn.controller;

import com.elearn.dto.OrderDto;
import com.elearn.dto.OrderRequest;
import com.elearn.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService  orderService;
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderRequest request) {
        logger.info("Creating order: {}", request);
        OrderDto order = orderService.createOrder(request);
        logger.info("Order created: {}", order);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/payment-url/{orderId}")
    public ResponseEntity<String> generatePaymentUrl(@PathVariable String orderId) {
        logger.info("Generating payment URL for orderId: {}", orderId);
        OrderDto order = orderService.getOrderById(orderId);
        String url = orderService.generatePaymentUrl(order);
        logger.info("Generated payment URL: {}", url);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/esewa/success")
    public ResponseEntity<OrderDto> handleEsewaSuccess(@RequestParam String refId, @RequestParam String pid, @RequestParam String amt) {
        logger.info("Esewa success callback received: refId={}, pid={}, amt={}", refId, pid, amt);
        OrderDto order = orderService.handleEsewaSuccessCallback(refId, pid, amt);
        logger.info("Esewa order processed: {}", order);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/esewa/failure")
    public ResponseEntity<String> handleEsewaFailure() {
        logger.warn("Esewa failure callback received.");
        return ResponseEntity.badRequest().body("Payment failed or cancelled.");
    }
}
