package in.hoprojects.eatmeowapi.repositories;

import in.hoprojects.eatmeowapi.models.OrderModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<OrderModel, String> {
    List<OrderModel> findByUserId(String userId);
    Optional<OrderModel> findByRazorpayOrderId(String razorpayOrderId);
}
