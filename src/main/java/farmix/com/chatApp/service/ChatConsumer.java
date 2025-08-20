package farmix.com.chatApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import farmix.com.chatApp.event.MessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

   @KafkaListener(topics = "chat-messages", groupId = "chat-app-group",concurrency = "3")
    public void consume(@Payload String message) {
       try {
           MessageEvent event = objectMapper.readValue(message, MessageEvent.class);
           System.out.println("Kafka consumed: " + message + " (sender=" + event.getSenderUsername() + ")");
       } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
       }
    }

}
