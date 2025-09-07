package farmix.com.chatApp.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String role;
    private List<MessageDTO> messages;

}
