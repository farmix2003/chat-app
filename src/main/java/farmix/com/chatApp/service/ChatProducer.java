package farmix.com.chatApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import farmix.com.chatApp.config.KafkaConfig;
import farmix.com.chatApp.event.MessageEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ChatProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(MessageEvent event) {
        try {
            event.setType("MessageEvent");
            String key = event.getConversationId() != null
                    ? String.valueOf(event.getConversationId())
                    : event.getSenderUsername();
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(KafkaConfig.TOPIC_NAME, key, payload);
            System.out.println("Kafka published: " + payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize MessageEvent", e);
        }
    }
}