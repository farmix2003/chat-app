package farmix.com.chatApp.controller;

import farmix.com.chatApp.entity.Message;
import farmix.com.chatApp.entity.MessageStatus;
import farmix.com.chatApp.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public Message sendMessage(
            @RequestParam Long conversationId,
            @RequestParam Long senderId,
            @RequestParam String content
    ) {
        return messageService.saveMessage(conversationId, senderId, content);
    }

    @GetMapping("/conversation/{conversationId}")
    public List<Message> getChatHistory(@PathVariable Long conversationId) {
        return messageService.getChatHistory(conversationId);
    }

    @GetMapping("/unread/{userId}")
    public List<MessageStatus> getUnreadMessages(@PathVariable Long userId) {
        return messageService.getUnreadMessages(userId);
    }

    @PutMapping("/{messageId}/read/{userId}")
    public void markAsRead(@PathVariable Long messageId, @PathVariable Long userId) {
        messageService.markAsRead(messageId, userId);
    }
}
