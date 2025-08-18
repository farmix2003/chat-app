package farmix.com.chatApp.repository;

import farmix.com.chatApp.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Long, Message> {
}
