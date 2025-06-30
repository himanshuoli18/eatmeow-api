package in.hoprojects.eatmeowapi.controllers;

import com.razorpay.RazorpayException;
import in.hoprojects.eatmeowapi.dtos.requests.OrderRequest;
import in.hoprojects.eatmeowapi.dtos.responses.OrderResponse;
import in.hoprojects.eatmeowapi.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrderWithPayment(@RequestBody OrderRequest request) throws RazorpayException {
        OrderResponse response = orderService.createOrderWithPayment(request);
        return new ResponseEntity<OrderResponse>(response, HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyPayment(@RequestBody Map<String, String> paymentData) {
        orderService.verifyPayment(paymentData,"Paid ");
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders() {
        return ResponseEntity.ok().body(orderService.getUserOrders());
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderId) {
        orderService.removeOrder(orderId);
        return ResponseEntity.noContent().build();
    }

//    Admin Panel methods
    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getOrdersOfAllUsers() {
        return ResponseEntity.ok().body(orderService.getOrdersOfAllUsers());
    }

    @PatchMapping("/status/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok().build();
    }
}
