package in.hoprojects.eatmeowapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Document(collection = "users")
public class UserModel {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;
}
