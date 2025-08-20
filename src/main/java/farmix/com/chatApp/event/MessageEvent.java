package farmix.com.chatApp.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEvent {

    private Long id;
    private String content;
    private Long conversationId;
    private String senderUsername;
    private LocalDateTime timestamp;

}
