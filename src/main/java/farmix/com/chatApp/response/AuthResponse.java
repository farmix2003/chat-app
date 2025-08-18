package farmix.com.chatApp.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String message;
}
