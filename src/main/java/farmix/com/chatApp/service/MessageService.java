package farmix.com.chatApp.service;

import farmix.com.chatApp.entity.Conversation;
import farmix.com.chatApp.entity.Message;
import farmix.com.chatApp.entity.MessageStatus;
import farmix.com.chatApp.entity.User;
import farmix.com.chatApp.repository.ConversationRepository;
import farmix.com.chatApp.repository.MessageRepository;
import farmix.com.chatApp.repository.MessageStatusRepository;
import farmix.com.chatApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageStatusRepository messageStatusRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "chat-messages";

    // 1. Save message & create message statuses
    public Message saveMessage(Long conversationId, Long senderId, String content) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(content)
                .createdAt(LocalDateTime.now())
                .messageType("TEXT")
                .build();

        Message saved = messageRepository.save(message);

        // Create statuses for all conversation participants
        conversation.getMembers().forEach(user -> {
            if (!user.getId().equals(sender.getId())) {
                MessageStatus status = MessageStatus.builder()
                        .message(saved)
                        .user(user.getUser())
                        .status("SENT")
                        .updatedAt(LocalDateTime.now())
                        .build();
                messageStatusRepository.save(status);
            }
        });

        // Publish to Kafka
        kafkaTemplate.send(TOPIC, saved);

        return saved;
    }

    // 2. Get chat history by conversation
    public List<Message> getChatHistory(Long conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    // 3. Get unread messages for a user
    public List<MessageStatus> getUnreadMessages(Long userId) {
        return messageStatusRepository.findByUserIdAndStatus(userId, "SENT");
    }

    // 4. Mark message as read
    public void markAsRead(Long messageId, Long userId) {
        MessageStatus status = messageStatusRepository.findByMessageIdAndUserId(messageId, userId);

        status.setStatus("READ");
        status.setUpdatedAt(LocalDateTime.now());
        messageStatusRepository.save(status);
    }
}
