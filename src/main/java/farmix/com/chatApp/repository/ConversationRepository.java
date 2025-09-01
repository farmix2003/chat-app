package farmix.com.chatApp.repository;

import farmix.com.chatApp.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("SELECT c FROM Conversation c LEFT JOIN FETCH c.members m LEFT JOIN FETCH m.user WHERE c.id = :id")
    Optional<Conversation> findByIdWithMembers(@Param("id") Long id);

    @Query("SELECT c FROM Conversation c LEFT JOIN FETCH c.members m WHERE m.user.id = :userId")
    List<Conversation> findAllByMembersId(@Param("userId") Long userId);
}