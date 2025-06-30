package in.hoprojects.eatmeowapi.controllers;

import in.hoprojects.eatmeowapi.dtos.requests.CartRequest;
import in.hoprojects.eatmeowapi.dtos.responses.CartResponse;
import in.hoprojects.eatmeowapi.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(@RequestBody CartRequest request) {
        String foodId = request.getFoodId();
        if (foodId == null || foodId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "foodId not found.");
        }
        return ResponseEntity.ok().body(cartService.addToCart(request) );
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
         return ResponseEntity.ok().body(cartService.getCart());
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/remove")
    public ResponseEntity<CartResponse> removeFromCart(@RequestBody CartRequest request) {
        String foodId = request.getFoodId();
        if (foodId == null || foodId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "foodId not found.");
        }
        return ResponseEntity.ok().body(cartService.removeFromCart(request));
    }
}
