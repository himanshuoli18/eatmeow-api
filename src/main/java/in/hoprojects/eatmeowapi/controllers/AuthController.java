package in.hoprojects.eatmeowapi.controllers;

import in.hoprojects.eatmeowapi.dtos.requests.LoginRequest;
import in.hoprojects.eatmeowapi.dtos.responses.LoginResponse;
import in.hoprojects.eatmeowapi.services.implementations.UserDetailsServiceImplementation;
import in.hoprojects.eatmeowapi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImplementation userDetailsServiceImplementation;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        final UserDetails userDetails = userDetailsServiceImplementation.loadUserByUsername(request.getEmail());
        final String jwtToken = jwtUtil.generateToken(userDetails);
        LoginResponse response = LoginResponse.builder()
                .email(request.getEmail())
                .token(jwtToken)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
