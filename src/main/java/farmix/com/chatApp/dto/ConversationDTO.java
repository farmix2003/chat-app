package farmix.com.chatApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConversationDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("memberIds")
    private List<Long> memberIds;
}