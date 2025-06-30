package in.hoprojects.eatmeowapi.repositories;

import in.hoprojects.eatmeowapi.models.CartModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<CartModel, String> {
    Optional<CartModel> findByUserId(String userId);
    void deleteByUserId(String userId);
}
