package muyu.trade.service;

import muyu.trade.entity.Notice;
import muyu.trade.model.Response;
import muyu.trade.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NoticeService {

    private NoticeRepository noticeRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository)
    {
        this.noticeRepository = noticeRepository;
    }

    public Response<List<Notice>> notice(Integer page, Integer size)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        return new Response<List<Notice>>(200, "", noticeRepository.findAll(pageable).getContent());
    }

    public Response<List<Notice>> allNotice(Integer page, Integer size)
    {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        return new Response<List<Notice>>(200, "", noticeRepository.findAll(pageable).getContent());
    }

    public Response<Object> remove(Integer id)
    {
        Notice notice = noticeRepository.findOne(id);
        if(notice == null)
            return new Response<>(400, "通知不存在", null);
        noticeRepository.delete(notice);
        return new Response<>();
    }

    public Response<Object> add(String content)
    {
        Notice notice = new Notice();
        notice.setContent(content);
        notice.setDate(new Date());
        noticeRepository.save(notice);
        return new Response<>();
    }

    public Integer count()
    {
        return Math.toIntExact(noticeRepository.count());
    }
}