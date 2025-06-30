package in.hoprojects.eatmeowapi.services;

import in.hoprojects.eatmeowapi.dtos.requests.LoginRequest;
import in.hoprojects.eatmeowapi.dtos.requests.RegisterRequest;
import in.hoprojects.eatmeowapi.dtos.responses.LoginResponse;
import in.hoprojects.eatmeowapi.dtos.responses.RegisterResponse;
import in.hoprojects.eatmeowapi.dtos.responses.UserResponse;

import java.util.List;

public interface UserService {
    RegisterResponse registerUser(RegisterRequest request);
    LoginResponse loginUser(LoginRequest request);
    String findByUserId();
    List<UserResponse> getAllUsers();
}
