package farmix.com.chatApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import farmix.com.chatApp.config.KafkaConfig;
import farmix.com.chatApp.dto.MessageDTO;
import farmix.com.chatApp.entity.User;
import farmix.com.chatApp.event.MessageEvent;
import farmix.com.chatApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatConsumer {
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    @Autowired
    public ChatConsumer(ObjectMapper objectMapper, SimpMessagingTemplate messagingTemplate, UserRepository userRepository) {
        this.objectMapper = objectMapper;
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = KafkaConfig.TOPIC_NAME, groupId = "chat-group")
    public void consume(String messageJson) {
        try {
            com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(messageJson);
            String type = jsonNode.has("type") ? jsonNode.get("type").asText() : null;

            if ("MessageEvent".equals(type) || type == null) {
                MessageEvent event = objectMapper.readValue(messageJson, MessageEvent.class);
                System.out.println("Received MessageEvent: " + event.getContent());
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setType("MessageDTO");
                messageDTO.setId(event.getId());
                messageDTO.setConversationId(event.getConversationId());
                messageDTO.setSenderId(getUserIdByUsername(event.getSenderUsername()));
                messageDTO.setContent(event.getContent());
                messageDTO.setMessageType("TEXT");
                messageDTO.setCreatedAt(event.getTimestamp());
                messagingTemplate.convertAndSend("/topic/conversations/" + event.getConversationId(), messageDTO);
            } else if ("MessageDTO".equals(type)) {
                MessageDTO messageDTO = objectMapper.readValue(messageJson, MessageDTO.class);
                System.out.println("Received MessageDTO: " + messageDTO.getContent());
                messagingTemplate.convertAndSend("/topic/conversations/" + messageDTO.getConversationId(), messageDTO);
            } else {
                System.out.println("Unknown message type: " + type);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize message: " + messageJson, e);
        }
    }

    private Long getUserIdByUsername(String username) {
        return userRepository.findByUsername(username).getId();
    }
}