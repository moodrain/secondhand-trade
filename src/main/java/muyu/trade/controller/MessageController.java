package muyu.trade.controller;

import muyu.trade.entity.Message;
import muyu.trade.entity.Notice;
import muyu.trade.model.Response;
import muyu.trade.service.MessageService;
import muyu.trade.service.NoticeService;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "api")
public class MessageController
{
    private NoticeService noticeService;
    private MessageService messageService;

    public MessageController(NoticeService noticeService, MessageService messageService) {
        this.noticeService = noticeService;
        this.messageService = messageService;
    }
    @GetMapping(value = "notice")
    public Response<List<Notice>> notice(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        return noticeService.notice(page, size);
    }
    @GetMapping(value = "messages")
    public Response<HashMap> messages(HttpSession session)
    {
        return messageService.messages(session);
    }
    @GetMapping(value = "chat-messages")
    public Response<List<Message>> chatMessages(HttpSession session, @RequestParam("id") Integer id, @RequestParam("messageId") Integer messageId) {
        return messageService.chatMessages(session, id, messageId);
    }
    @PostMapping(value = "send-message")
    public Response<Object> sendMessage(HttpSession session, @RequestParam("id") Integer id, @RequestParam("content") String content) {
        return messageService.sendMessage(session, id, content);
    }
}