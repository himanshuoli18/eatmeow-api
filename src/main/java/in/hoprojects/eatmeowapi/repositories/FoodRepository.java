package in.hoprojects.eatmeowapi.repositories;

import in.hoprojects.eatmeowapi.models.FoodModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends MongoRepository<FoodModel, String> {

}
