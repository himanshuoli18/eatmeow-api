package in.hoprojects.eatmeowapi.controllers;

import in.hoprojects.eatmeowapi.dtos.requests.RegisterRequest;
import in.hoprojects.eatmeowapi.dtos.responses.RegisterResponse;
import in.hoprojects.eatmeowapi.dtos.responses.UserResponse;
import in.hoprojects.eatmeowapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(request));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

}
