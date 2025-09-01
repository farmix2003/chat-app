package farmix.com.chatApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO {
    @JsonProperty("type")
    private String type = "MessageDTO";

    @JsonProperty("id")
    private Long id;

    @JsonProperty("conversationId")
    private Long conversationId;

    @JsonProperty("senderId")
    private Long senderId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("messageType")
    private String messageType;

    @JsonProperty("mediaUrl")
    private String mediaUrl;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
}