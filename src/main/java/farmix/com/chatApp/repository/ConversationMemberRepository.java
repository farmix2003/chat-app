package farmix.com.chatApp.repository;

import farmix.com.chatApp.entity.ConversationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationMemberRepository extends JpaRepository<Long, ConversationMember> {
}
