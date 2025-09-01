package farmix.com.chatApp.service;

import farmix.com.chatApp.dto.ConversationDTO;
import farmix.com.chatApp.entity.Conversation;
import farmix.com.chatApp.entity.ConversationMember;
import farmix.com.chatApp.entity.User;
import farmix.com.chatApp.repository.ConversationRepository;
import farmix.com.chatApp.repository.UserRepository;
import farmix.com.chatApp.request.ConversationRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    public ConversationService(ConversationRepository conversationRepository, UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }

    public ConversationDTO createConversation(ConversationRequest request, List<Long> userIds) {
        List<User> participants = userRepository.findAllById(userIds);

        if (participants.size() < 2 || participants.size() != userIds.size()) {
            throw new RuntimeException("Conversation must have at least 2 participants/Some users not found");
        }

        Conversation newConversation = new Conversation();
        newConversation.setName(request.getName());
        newConversation.setType(request.getType());
        newConversation.setCreatedAt(LocalDateTime.now());

        List<ConversationMember> members = new ArrayList<>();

        participants.forEach(participant -> {
            ConversationMember member = new ConversationMember();
            member.setUser(participant);
            member.setConversation(newConversation);
            member.setJoinedAt(LocalDateTime.now());
            members.add(member);
        });

        newConversation.setMembers(members);

        Conversation saved = conversationRepository.save(newConversation);

        return mapToDto(saved);
      }

    public ConversationDTO getConversationById(Long id){

        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        return mapToDto(conversation);
    }

    public List<ConversationDTO> getUserConversations(Long userId){
        return conversationRepository.findAllByMembersId(userId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public void deleteConversation(Long conversationId){
        conversationRepository.deleteById(conversationId);
    }
public ConversationDTO mapToDto(Conversation conversation){
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setId(conversation.getId());
        conversationDTO.setName(conversation.getName());
        conversationDTO.setType(conversation.getType());
        conversationDTO.setCreatedAt(conversation.getCreatedAt());
        conversationDTO.setMemberIds(conversation.getMembers().stream().map(ConversationMember::getUser).map(User::getId).toList());
        return conversationDTO;
}
}
