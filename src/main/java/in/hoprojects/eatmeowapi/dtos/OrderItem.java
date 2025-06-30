package in.hoprojects.eatmeowapi.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItem {
    private String foodId;
    private String name;
    private String description;
    private String category;
    private String imageUrl;
    private double price;
    private int quantity;
}
