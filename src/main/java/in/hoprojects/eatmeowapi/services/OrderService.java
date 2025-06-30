package in.hoprojects.eatmeowapi.services;

import com.razorpay.RazorpayException;
import in.hoprojects.eatmeowapi.dtos.requests.OrderRequest;
import in.hoprojects.eatmeowapi.dtos.responses.OrderResponse;

import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException;
    void verifyPayment(Map<String, String> paymentData, String status);
    List<OrderResponse> getUserOrders();
    void removeOrder(String orderId);

//    Admin Panel Methods
    List<OrderResponse> getOrdersOfAllUsers();
    void updateOrderStatus(String orderId, String status);
}
