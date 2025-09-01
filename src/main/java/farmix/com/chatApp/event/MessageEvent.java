package farmix.com.chatApp.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageEvent {
    @JsonProperty("type")
    private String type = "MessageEvent";

    @JsonProperty("id")
    private Long id;

    @JsonProperty("conversationId")
    private Long conversationId;

    @JsonProperty("senderUsername")
    private String senderUsername;

    @JsonProperty("content")
    private String content;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
}