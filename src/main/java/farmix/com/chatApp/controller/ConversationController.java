package farmix.com.chatApp.controller;

import farmix.com.chatApp.dto.ConversationDTO;
import farmix.com.chatApp.entity.Conversation;
import farmix.com.chatApp.request.ConversationRequest;
import farmix.com.chatApp.service.ConversationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    public ResponseEntity<ConversationDTO> createConversation(@RequestBody ConversationRequest req) {
        ConversationDTO conversation = conversationService.createConversation(req, req.getUserIds());
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDTO> getConversationById(@PathVariable Long id) {
        ConversationDTO conversation = conversationService.getConversationById(id);
        return ResponseEntity.ok(conversation);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ConversationDTO>> getUserConversations(@PathVariable Long userId) {
        List<ConversationDTO> conversations = conversationService.getUserConversations(userId);
        return ResponseEntity.ok(conversations);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConversation(@PathVariable Long id) {
        conversationService.deleteConversation(id);
        return ResponseEntity.ok("Conversation deleted successfully");
    }

}
