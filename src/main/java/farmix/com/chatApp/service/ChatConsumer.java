package farmix.com.chatApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatConsumer {

   @KafkaListener(topics = "chat-messages", groupId = "chat-app-group")
    public void consume(String message) {
        System.out.println("Received Message: " + message);
    }

}
