package in.hoprojects.eatmeowapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.hoprojects.eatmeowapi.dtos.requests.FoodRequest;
import in.hoprojects.eatmeowapi.dtos.responses.FoodResponse;
import in.hoprojects.eatmeowapi.services.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FoodController {
    private final FoodService foodService;
    private final ObjectMapper objectMapper;

    @PostMapping()
    public ResponseEntity<FoodResponse> addFood(@RequestPart("food") String foodString, @RequestPart("file")MultipartFile file) {
        FoodRequest request = null;
        try {
            request = objectMapper.readValue(foodString, FoodRequest.class );
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format.");
        }
        FoodResponse savedFood = foodService.addFood(request, file);
        return new ResponseEntity<FoodResponse>(savedFood, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FoodResponse>> getAllFoods() {
        return new ResponseEntity<>(foodService.getAllFoods(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodResponse> getFood(@PathVariable String id) {
        return new ResponseEntity<>(foodService.getFood(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable String id) {
        foodService.deleteFood(id);
    }
}
