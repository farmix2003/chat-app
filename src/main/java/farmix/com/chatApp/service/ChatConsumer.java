package farmix.com.chatApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import farmix.com.chatApp.config.KafkaConfig;
import farmix.com.chatApp.dto.MessageDTO;
import farmix.com.chatApp.event.MessageEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ChatConsumer {
    private final ObjectMapper objectMapper;

    public ChatConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = KafkaConfig.TOPIC_NAME, groupId = "chat-group")
    public void consume(String messageJson) {
        try {
            // Parse JSON to check type
            com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(messageJson);
            String type = jsonNode.has("type") ? jsonNode.get("type").asText() : null;

            if ("MessageEvent".equals(type) || type == null) { // Fallback for MessageEvent without type
                MessageEvent event = objectMapper.readValue(messageJson, MessageEvent.class);
                System.out.println("Received MessageEvent: " + event.getContent());
                // Process MessageEvent
            } else if ("MessageDTO".equals(type)) {
                MessageDTO message = objectMapper.readValue(messageJson, MessageDTO.class);
                System.out.println("Received MessageDTO: " + message.getContent());
                // Process MessageDTO
            } else {
                System.out.println("Unknown message type: " + type);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize message: " + messageJson, e);
        }
    }
}