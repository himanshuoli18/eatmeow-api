package in.hoprojects.eatmeowapi.services.implementations;

import in.hoprojects.eatmeowapi.dtos.requests.FoodRequest;
import in.hoprojects.eatmeowapi.dtos.responses.FoodResponse;
import in.hoprojects.eatmeowapi.models.FoodModel;
import in.hoprojects.eatmeowapi.repositories.FoodRepository;
import in.hoprojects.eatmeowapi.services.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodServiceImplementation implements FoodService {
    private final S3Client s3Client;
    private final FoodRepository foodRepository;

    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Override
    public String uploadFile(MultipartFile file) {
        String fileNameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String key =  UUID.randomUUID().toString()+"."+fileNameExtension;
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();
            PutObjectResponse response = s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
            if (response.sdkHttpResponse().isSuccessful()) {
                return "https://"+bucketName+".s3.amazonaws.com/"+key;
            }
            else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed.");
            }
        }
        catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while uploading the file.");
        }
    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {
        FoodModel newFood = convertDtoToModel(request);
        String imageUrl = uploadFile(file);
        newFood.setImageUrl(imageUrl);
        return convertModelToDto(foodRepository.save(newFood));
    }

    @Override
    public List<FoodResponse> getAllFoods() {
        List<FoodModel> allFoods = foodRepository.findAll();
        return allFoods.stream()
                .map(item -> convertModelToDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public FoodResponse getFood(String id) {
        FoodModel existingFood = foodRepository.findById(id). orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found with id:"+id));
        return convertModelToDto(existingFood);
    }

    @Override
    public boolean deleteFileFromS3(String fileName) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        s3Client.deleteObject(request);
        return true;
    }

    @Override
    public void deleteFood(String id) {
        FoodResponse response = getFood(id);
        String imageUrl = response.getImageUrl();
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        boolean isFileDeleted = deleteFileFromS3(fileName);
        if (isFileDeleted) {
            foodRepository.deleteById(response.getId());
        }
    }

    private FoodModel convertDtoToModel(FoodRequest request) {
        return FoodModel.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .build();
    }

    private FoodResponse convertModelToDto(FoodModel food) {
        return FoodResponse.builder()
                .id(food.getId())
                .name(food.getName())
                .description(food.getDescription())
                .price(food.getPrice())
                .category(food.getCategory())
                .imageUrl(food.getImageUrl())
                .build();
    }
}
