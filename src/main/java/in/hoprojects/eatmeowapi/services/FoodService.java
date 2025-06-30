package in.hoprojects.eatmeowapi.services;

import in.hoprojects.eatmeowapi.dtos.requests.FoodRequest;
import in.hoprojects.eatmeowapi.dtos.responses.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {
    String uploadFile(MultipartFile file);
    FoodResponse addFood(FoodRequest request, MultipartFile file);
    List<FoodResponse> getAllFoods();
    FoodResponse getFood(String id);
    boolean deleteFileFromS3(String fileName);
    void deleteFood(String id);
}
