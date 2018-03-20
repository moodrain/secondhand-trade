package muyu.trade.service;

import muyu.trade.entity.Message;
import muyu.trade.entity.User;
import muyu.trade.model.Response;
import muyu.trade.repository.MessageRepository;
import muyu.trade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    @Autowired
    public MessageService(MessageRepository messageRepository, UserRepository userRepository)
    {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public Response<HashMap> messages(HttpSession session)
    {
        User user = (User)session.getAttribute("user");
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(0, 100, sort);
        HashMap hash = new HashMap<>();
        hash.put("id", ((User)session.getAttribute("user")).getId());
        hash.put("data", messageRepository.findMessagesByFromOrTo(user, user, pageable));
        return new Response<>(200, "", hash);
    }

    public Response<List<Message>> chatMessages(HttpSession session, Integer id, Integer messageId)
    {
        User user = (User)session.getAttribute("user");
        User another = userRepository.findOne(id);
        return new Response<>(200, "", messageRepository.getChatMessage(messageId, user.getId(), another.getId()));
    }

    public Response<Object> sendMessage(HttpSession session, Integer id, String content)
    {
        User from = (User)session.getAttribute("user");
        User to = userRepository.findOne(id);
        if(to == null)
            return new Response<>(400, "用户不存在", null);
        Message msg = new Message();
        msg.setContent(content);
        msg.setDate(new Date());
        msg.setFrom(from);
        msg.setIsRead(0);
        msg.setIsSystem(0);
        msg.setTo(to);
        messageRepository.save(msg);
        return new Response<>();
    }

    public Boolean sendNoticeMessage(Integer fromId, Integer toId, String content)
    {
        User from;
        if(fromId.equals(0))
        {
             from = new User();
            from.setId(0);
        }
        else
            from = userRepository.findOne(fromId);
        User to = userRepository.findOne(toId);
        if(to == null || from == null)
            return false;
        Message message = new Message();
        message.setTo(to);
        message.setIsSystem(1);
        message.setIsRead(0);
        message.setFrom(from);
        message.setDate(new Date());
        message.setContent(content);
        messageRepository.save(message);
        return true;
    }
}
