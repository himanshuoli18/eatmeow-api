package in.hoprojects.eatmeowapi.services;

import in.hoprojects.eatmeowapi.dtos.requests.CartRequest;
import in.hoprojects.eatmeowapi.dtos.responses.CartResponse;

public interface CartService {
    CartResponse addToCart(CartRequest request);
    CartResponse getCart();
    void clearCart();
    CartResponse removeFromCart(CartRequest request);
}
