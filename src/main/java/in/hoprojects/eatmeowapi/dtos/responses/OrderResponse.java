package in.hoprojects.eatmeowapi.dtos.responses;

import in.hoprojects.eatmeowapi.dtos.OrderItem;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private String id;
    private String userId;
    private String userAddress;
    private String phoneNumber;
    private String email;
    private double amount;
    private String paymentStatus;
    private String orderStatus;
    private List<OrderItem> orderedItems;
    private LocalDateTime createdAt;
    private String razorpayOrderId;
 }
