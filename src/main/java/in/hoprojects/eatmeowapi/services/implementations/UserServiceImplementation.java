package in.hoprojects.eatmeowapi.services.implementations;

import in.hoprojects.eatmeowapi.dtos.requests.LoginRequest;
import in.hoprojects.eatmeowapi.dtos.requests.RegisterRequest;
import in.hoprojects.eatmeowapi.dtos.responses.LoginResponse;
import in.hoprojects.eatmeowapi.dtos.responses.RegisterResponse;
import in.hoprojects.eatmeowapi.dtos.responses.UserResponse;
import in.hoprojects.eatmeowapi.models.UserModel;
import in.hoprojects.eatmeowapi.repositories.UserRepository;
import in.hoprojects.eatmeowapi.services.AuthenticationFacade;
import in.hoprojects.eatmeowapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public RegisterResponse registerUser(RegisterRequest request) {
        UserModel newUser = convertDtoToModel(request);
        newUser = userRepository.save(newUser);
        return convertModelToDto(newUser);
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        return null;
    }

    @Override
    public String findByUserId() {
        String loggedInUserEmail = authenticationFacade.getAuthentication().getName();
        UserModel loggedInUser = userRepository.findByEmail(loggedInUserEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return loggedInUser.getId();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<UserModel> allUsers = userRepository.findAll();
        return allUsers.stream().map(this::convertModelToUserDto).toList();
    }

    private UserResponse convertModelToUserDto(UserModel userModel) {
        return UserResponse.builder()
                .id(userModel.getId())
                .name(userModel.getName())
                .email(userModel.getEmail())
                .createdAt(userModel.getCreatedAt())
                .build();
    }

    private UserModel convertDtoToModel(RegisterRequest request){
        return UserModel.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
    }
    private RegisterResponse convertModelToDto(UserModel registeredUser) {
        return RegisterResponse.builder()
                .id(registeredUser.getId())
                .name(registeredUser.getName())
                .email(registeredUser.getEmail())
                .build();
    }
}
