package farmix.com.chatApp.repository;

import farmix.com.chatApp.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m " +
            "LEFT JOIN FETCH m.conversation c " +
            "LEFT JOIN FETCH m.sender s " +
            "WHERE m.conversation.id = :conversationId " +
            "ORDER BY m.createdAt ASC")
    List<Message> findByConversationIdOrderByCreatedAtAsc(@Param("conversationId") Long conversationId);
}