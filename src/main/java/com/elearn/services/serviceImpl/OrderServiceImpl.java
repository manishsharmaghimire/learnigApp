package com.elearn.services.serviceImpl;

import com.elearn.dto.OrderDto;
import com.elearn.dto.OrderRequest;
import com.elearn.entity.Course;
import com.elearn.entity.Order;
import com.elearn.entity.User;
import com.elearn.exception.ResourceNotFoundException;
import com.elearn.repository.CourseRepo;
import com.elearn.repository.OrderRepository;
import com.elearn.repository.UserRepository;
import com.elearn.services.OrderService;
import com.elearn.exception.PaymentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final CourseRepo courseRepo;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;

    @Value("${esewa.merchant.code}")
    public String merchantCode;
    @Value("${esewa.urls.success}")
    private String successUrl;

    @Value("${esewa.urls.failure}")
    private String failureUrl;


    @Value("${esewa.urls.verification}")
    private String verificationUrl; // Added missing verification URL

    // ... existing getCourse() and getUser() methods ...

    /**
     * @param orderRequest
     * @return
     */
    @Override
    public Course getCourse(OrderRequest orderRequest) {
        return null;
    }

    /**
     * @param orderRequest
     * @return
     */
    @Override
    public User getUser(OrderRequest orderRequest) {
        return null;
    }

    @Override
    public OrderDto createOrder(OrderRequest orderRequest) {
        // Validate input
        if (orderRequest.getCourseId() == null || orderRequest.getCourseId().isBlank() ||
                orderRequest.getUserId() == null || orderRequest.getUserId().isBlank()) {
            throw new IllegalArgumentException("Course ID and User ID must not be null or empty");
        }

        if (orderRequest.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (orderRequest.getAddress() == null || orderRequest.getAddress().isBlank()) {
            throw new IllegalArgumentException("Address is required");
        }

        // Get course and user
        Course course = courseRepo.findById(orderRequest.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + orderRequest.getCourseId()));

        User user = userRepo.findById(orderRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + orderRequest.getUserId()));

        // Generate unique IDs
        String orderId = UUID.randomUUID().toString();
        String esewaOrderId = "ESW_" + LocalDate.now() + "_" + UUID.randomUUID().toString().substring(0, 8);

        // Build and save order
        Order order = Order.builder()
                .orderId(orderId)
                .esewaOrderId(esewaOrderId)
                .amount(orderRequest.getAmount())
                .address(orderRequest.getAddress())
                .course(course)
                .user(user)
                .pmtStatus("PENDING") // Changed to uppercase for consistency
                .createdDate(new Date())
                .build();

        Order savedOrder = orderRepo.save(order);
        return modelMapper.map(savedOrder, OrderDto.class);
    }

    /**
     * @param paymentId
     * @param orderId
     * @return
     */
    @Override
    public OrderDto verifyPayment(String paymentId, String orderId) {
        return null;
    }

    @Override
    public String generatePaymentUrl(OrderDto orderDto) {
        try {
            // Validate order DTO
            if (orderDto == null || orderDto.getEsewaOrderId() == null) {
                throw new IllegalArgumentException("Invalid order data");
            }

            // Create parameter map
            Map<String, String> params = new LinkedHashMap<>();
            params.put("amt", String.format("%.2f", orderDto.getAmount())); // Format amount to 2 decimal places
            params.put("pid", orderDto.getEsewaOrderId());
            params.put("scd", merchantCode);
            params.put("su", successUrl);
            params.put("fu", failureUrl);

            // Optional parameters with default values
            params.put("pdc", "0");
            params.put("psc", "0");
            params.put("txAmt", "0");

            // Build query string
            String queryParams = params.entrySet().stream()
                    .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));

            // Construct final URL
            String finalUrl = "https://esewa.com.np/epay/main?" + queryParams;
            log.info("Generated eSewa payment URL for PID: {}", orderDto.getEsewaOrderId());
            return finalUrl;
        } catch (Exception e) {
            log.error("Payment URL generation failed for order: {}", orderDto != null ? orderDto.getEsewaOrderId() : "null", e);
            throw new RuntimeException("Failed to generate payment URL", e);
        }
    }

    @Override
    public boolean verifyEsewaTransaction(String refId, String pid, String amt) {
        if (refId == null || pid == null || amt == null) {
            log.error("Verification failed: missing parameters");
            return false;
        }

        try {
            // Prepare request body
            String requestBody = String.format(
                    "amt=%s&scd=%s&pid=%s&rid=%s",
                    URLEncoder.encode(amt, StandardCharsets.UTF_8),
                    URLEncoder.encode(merchantCode, StandardCharsets.UTF_8),
                    URLEncoder.encode(pid, StandardCharsets.UTF_8),
                    URLEncoder.encode(refId, StandardCharsets.UTF_8)
            );

            // Configure connection
            HttpURLConnection conn = (HttpURLConnection) new URL(verificationUrl).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes(StandardCharsets.UTF_8));
            }

            // Read response
            String response;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                response = reader.lines().collect(Collectors.joining());
            }

            // Verify response
            boolean isSuccess = response.contains("<response_code>Success</response_code>");
            if (isSuccess) {
                log.info("Payment verified successfully for PID: {}", pid);
            } else {
                log.warn("Payment verification failed for PID: {}", pid);
            }
            return isSuccess;
        } catch (Exception e) {
            log.error("Verification failed for PID: {}", pid, e);
            return false;
        }
    }

    @Override
    public OrderDto handleEsewaSuccessCallback(String refId, String pid, String amt) {
        // Validate parameters
        if (refId == null || refId.isEmpty() ||
                pid == null || pid.isEmpty() ||
                amt == null || amt.isEmpty()) {
            throw new IllegalArgumentException("Invalid callback parameters");
        }

        log.info("Processing eSewa callback for PID: {}, RefID: {}", pid, refId);

        // Find order
        Order order = orderRepo.findByEsewaOrderId(pid).orElseThrow(() -> new ResourceNotFoundException("Order not found: " + pid));

        // Verify amount (with 0.01 tolerance for floating point)
        double paymentAmount = Double.parseDouble(amt);
        if (Math.abs(paymentAmount - order.getAmount()) > 0.01) {
            log.error("Amount mismatch for order {}: expected {}, received {}",
                    pid, order.getAmount(), paymentAmount);
            throw new PaymentException("Amount mismatch for order: " + pid);
        }

        // Verify payment with eSewa
        if (!verifyEsewaTransaction(refId, pid, amt)) {
            log.error("Payment verification failed for PID: {}", pid);
            throw new PaymentException("Payment verification failed for: " + pid);
        }

        // Update order status
        order.setPmtStatus("PAID");
        Order savedOrder = orderRepo.save(order);

        log.info("Payment completed for order: {}", pid);
        return modelMapper.map(savedOrder, OrderDto.class);
    }

    // Add this method if not already present
    @Override
    public OrderDto getOrderById(String orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return modelMapper.map(order, OrderDto.class);
    }
}