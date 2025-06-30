package in.hoprojects.eatmeowapi.dtos.responses;

import lombok.*;

@AllArgsConstructor
@Getter
@Builder
public class LoginResponse {
    private String email;
    private String token;
}
