package com.elearn.testserv;

import com.elearn.controller.OrderController;
import com.elearn.dto.OrderDto;
import com.elearn.services.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import({OrderControllerTest.TestConfig.class}) // Import test configuration
@ExtendWith(MockitoExtension.class) // Enable Mockito support
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    // Configuration to provide the controller with mocked dependencies
    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public OrderController orderController(OrderService orderService) {
            return new OrderController(orderService);
        }
    }

    @Test
    void testEsewaSuccessCallback() throws Exception {
        // Test data
        String refId = "test_ref_123";
        String pid = "ESW_2023-06-15_test123";
        String amt = "1000.00";

        // Mock response
        OrderDto mockResponse = OrderDto.builder()
                .orderId("order_123")
                .esewaOrderId(pid)
                .amount(Double.parseDouble(amt))
                .pmtStatus("PAID")
                .build();

        // Define mock behavior
        when(orderService.handleEsewaSuccessCallback(refId, pid, amt))
                .thenReturn(mockResponse);

        // Execute request and verify
        mockMvc.perform(get("/api/v1/orders/esewa/success")
                        .param("refId", refId)
                        .param("pid", pid)
                        .param("amt", amt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("order_123"))
                .andExpect(jsonPath("$.pmtStatus").value("PAID"))
                .andExpect(jsonPath("$.amount").value(1000.00))
                .andExpect(jsonPath("$.esewaOrderId").value(pid));

        // Verify service was called
        verify(orderService).handleEsewaSuccessCallback(refId, pid, amt);
    }
}