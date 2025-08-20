package farmix.com.chatApp.repository;

import farmix.com.chatApp.entity.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageStatusRepository extends JpaRepository<MessageStatus, Long> {
    List<MessageStatus> findByUserIdAndStatus(Long userId, String sent);
    MessageStatus findByMessageIdAndUserId(Long messageId, Long userId);
}
