package in.hoprojects.eatmeowapi.services.implementations;

import in.hoprojects.eatmeowapi.dtos.requests.CartRequest;
import in.hoprojects.eatmeowapi.dtos.responses.CartResponse;
import in.hoprojects.eatmeowapi.models.CartModel;
import in.hoprojects.eatmeowapi.repositories.CartRepository;
import in.hoprojects.eatmeowapi.services.CartService;
import in.hoprojects.eatmeowapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImplementation implements CartService {

    private final CartRepository cartRepository;
    private final UserService userService;

    @Override
    public CartResponse addToCart(CartRequest request) {
        String loggedInUserId = userService.findByUserId();
        Optional<CartModel> cartOptional = cartRepository.findByUserId(loggedInUserId);
        CartModel cart = cartOptional.orElseGet(() -> new CartModel(loggedInUserId, new HashMap<>()));
        Map<String, Integer> cartItems = cart.getItems();
        cartItems.put(request.getFoodId(), cartItems.getOrDefault(request.getFoodId(), 0) + 1);
        cart.setItems(cartItems);
        return convertModelToDto(cartRepository.save(cart));
    }

    @Override
    public CartResponse getCart() {
        String userId = userService.findByUserId();
        CartModel cart = cartRepository.findByUserId(userId).orElse(new CartModel(null, userId,new HashMap<>()));
        return convertModelToDto(cart);
    }

    @Override
    public void clearCart() {
        String loggedInUser = userService.findByUserId();
        cartRepository.deleteByUserId(loggedInUser);
    }


    @Override
    public CartResponse removeFromCart(CartRequest request) {
        String userId = userService.findByUserId();
        CartModel model = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found."));
        Map<String, Integer> cartItems = model.getItems();
        if (cartItems.containsKey(request.getFoodId())) {
            int currentQty = cartItems.get(request.getFoodId());
            if (currentQty > 1) {
                cartItems.put(request.getFoodId(), currentQty - 1);
            }
            else {
                cartItems.remove(request.getFoodId());
            }
            cartRepository.save(model);
        }
        return  convertModelToDto(model);
    }

    private CartResponse convertModelToDto(CartModel model) {
        return CartResponse.builder()
                .id(model.getId())
                .userId(model.getUserId())
                .items(model.getItems())
                .build();
    }
}
