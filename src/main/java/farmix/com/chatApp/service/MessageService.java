package farmix.com.chatApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import farmix.com.chatApp.dto.MessageDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageStatusRepository messageStatusRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "chat-messages";

    @Transactional
    public MessageDTO saveMessage(Long conversationId, Long senderId, String content) {
        Conversation conversation = conversationRepository.findByIdWithMembers(conversationId)
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

        // Create statuses for all conversation participants except the sender
        conversation.getMembers().forEach(member -> {
            if (!member.getUser().getId().equals(sender.getId())) {
                MessageStatus status = MessageStatus.builder()
                        .message(saved)
                        .user(member.getUser())
                        .status("SENT")
                        .updatedAt(LocalDateTime.now())
                        .build();
                messageStatusRepository.save(status);
            }
        });

        // Convert to DTO
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setType("MessageDTO");
        messageDTO.setId(saved.getId());
        messageDTO.setConversationId(saved.getConversation().getId());
        messageDTO.setSenderId(saved.getSender().getId());
        messageDTO.setContent(saved.getContent());
        messageDTO.setMessageType(saved.getMessageType());
        messageDTO.setMediaUrl(saved.getMediaUrl());
        messageDTO.setCreatedAt(saved.getCreatedAt());

        // Publish to Kafka
        try {
            String key = String.valueOf(conversationId);
            String payload = objectMapper.writeValueAsString(messageDTO);
            kafkaTemplate.send(TOPIC, key, payload);
            System.out.println("Kafka published: " + payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize MessageDTO", e);
        }

        return messageDTO;
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> getChatHistory(Long conversationId) {
        List<Message> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        return messages.stream()
                .map(message -> {
                    MessageDTO dto = new MessageDTO();
                    dto.setType("MessageDTO");
                    dto.setId(message.getId());
                    dto.setConversationId(message.getConversation().getId());
                    dto.setSenderId(message.getSender().getId());
                    dto.setContent(message.getContent());
                    dto.setMessageType(message.getMessageType());
                    dto.setMediaUrl(message.getMediaUrl());
                    dto.setCreatedAt(message.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MessageStatus> getUnreadMessages(Long userId) {
        return messageStatusRepository.findByUserIdAndStatus(userId, "SENT");
    }

    @Transactional
    public void markAsRead(Long messageId, Long userId) {
        MessageStatus status = messageStatusRepository.findByMessageIdAndUserId(messageId, userId);
        status.setStatus("READ");
        status.setUpdatedAt(LocalDateTime.now());
        messageStatusRepository.save(status);
    }
}