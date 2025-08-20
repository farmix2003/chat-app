package farmix.com.chatApp.request;

import lombok.Data;

import java.util.List;

@Data
public class ConversationRequest {
    private String name;
    private String type;
    private List<Long> userIds;
}
