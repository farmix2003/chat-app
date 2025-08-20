package farmix.com.chatApp.controller;

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
    public ResponseEntity<Conversation> createConversation(@RequestBody ConversationRequest req) {
        Conversation conversation = conversationService.createConversation(req, req.getUserIds());
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conversation> getConversationById(@PathVariable Long id) {
        Conversation conversation = conversationService.getConversationById(id);
        return ResponseEntity.ok(conversation);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Conversation>> getUserConversations(@PathVariable Long userId) {
        List<Conversation> conversations = conversationService.getUserConversations(userId);
        return ResponseEntity.ok(conversations);
    }

}
