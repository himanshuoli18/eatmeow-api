package in.hoprojects.eatmeowapi.services.implementations;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import in.hoprojects.eatmeowapi.dtos.requests.OrderRequest;
import in.hoprojects.eatmeowapi.dtos.responses.OrderResponse;
import in.hoprojects.eatmeowapi.models.OrderModel;
import in.hoprojects.eatmeowapi.repositories.CartRepository;
import in.hoprojects.eatmeowapi.repositories.OrderRepository;
import in.hoprojects.eatmeowapi.services.OrderService;
import in.hoprojects.eatmeowapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserService userService;

    @Value("${razorpay.access.key}")
    private String RAZORPAY_ACCESS_KEY;

    @Value("${razorpay.secret.key}")
    private String RAZORPAY_SECRET_KEY;

    @Override
    public OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException {
        OrderModel newOrder = convertDtoToModel(request);
        String loggedInUserId = userService.findByUserId();
        newOrder.setUserId(loggedInUserId);

        // Convert amount to paise (Razorpay requires smallest currency unit)
        int amountInPaise = (int) (newOrder.getAmount() * 100);

        // Create Razorpay order
        RazorpayClient client = new RazorpayClient(RAZORPAY_ACCESS_KEY, RAZORPAY_SECRET_KEY);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("payment_capture", 1);

        Order razorpayOrder = client.orders.create(orderRequest);
        String razorpayOrderId = razorpayOrder.get("id");

        // Save Razorpay order ID and persist order
        newOrder.setRazorpayOrderId(razorpayOrderId);
        orderRepository.save(newOrder);

        // Return response DTO
        return convertModelToDto(newOrder);
    }

    @Override
    public void verifyPayment(Map<String, String> paymentData, String status) {
        String razorpayOrderId = paymentData.get("razorpay_order_id");
        System.out.println("🔍 Incoming Razorpay Order ID: " + razorpayOrderId);
        OrderModel existingOrder = orderRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        existingOrder.setPaymentStatus(status);
        existingOrder.setRazorpaySignature(paymentData.get("razorpay_signature"));
        existingOrder.setRazorpayPaymentId(paymentData.get("razorpay_payment_id"));
        orderRepository.save(existingOrder);
        if ("paid".equalsIgnoreCase(status)) {
            cartRepository.deleteByUserId(existingOrder.getUserId());
        }
    }



    @Override
    public List<OrderResponse> getUserOrders() {
        String loggedInUserId = userService.findByUserId();
        return orderRepository.findByUserId(loggedInUserId)
                .stream()
                .map(this::convertModelToDto)
                .toList();
    }

    @Override
    public void removeOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderResponse> getOrdersOfAllUsers() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertModelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
        OrderModel model = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found."));
        model.setOrderStatus(status);
        orderRepository.save(model);
    }

    private OrderModel convertDtoToModel(OrderRequest request) {
        return OrderModel.builder()
                .orderedItems(request.getOrderedItems())
                .userAddress(request.getUserAddress())
                .amount(request.getAmount())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .orderStatus(request.getOrderStatus())
                .build();
    }

    private OrderResponse convertModelToDto(OrderModel model) {
        return OrderResponse.builder()
                .id(model.getId())
                .userId(model.getUserId())
                .userAddress(model.getUserAddress())
                .phoneNumber(model.getPhoneNumber())
                .email(model.getEmail())
                .amount(model.getAmount())
                .paymentStatus(model.getPaymentStatus())
                .orderStatus(model.getOrderStatus())
                .orderedItems(model.getOrderedItems())
                .createdAt(model.getCreatedAt())
                .razorpayOrderId(model.getRazorpayOrderId())
                .build();
    }
}
