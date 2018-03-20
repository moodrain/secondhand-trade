package muyu.trade.repository;

import muyu.trade.entity.Message;
import muyu.trade.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    public List<Message> findMessagesByFromOrTo(User from, User to, Pageable pageable);
    @Query(value = "select * from message where id > ?1 and ((from_id = ?2 and to_id = ?3) or (from_id = ?3 and to_id = ?2))", nativeQuery = true)
    public List<Message> getChatMessage(Integer messageId, Integer from, Integer to);
    public List<Message> findMessagesByTo(User to, Pageable pageable);

}
