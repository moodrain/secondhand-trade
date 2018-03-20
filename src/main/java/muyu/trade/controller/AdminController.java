package muyu.trade.controller;

import muyu.trade.entity.*;
import muyu.trade.model.Response;
import muyu.trade.model.TableResponse;
import muyu.trade.service.GoodsService;
import muyu.trade.service.NoticeService;
import muyu.trade.service.OrderService;
import muyu.trade.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/admin/")
public class AdminController {

    private GoodsService goodsService;
    private OrderService orderService;
    private UserService userService;
    private NoticeService noticeService;

    @Autowired
    public AdminController(GoodsService goodsService, OrderService orderService, UserService userService, NoticeService noticeService)
    {
        this.goodsService = goodsService;
        this.orderService = orderService;
        this.userService = userService;
        this.noticeService = noticeService;
    }

    @GetMapping(value = "goods")
    public TableResponse<List<Goods>> goods(@RequestParam("page") Integer page, @RequestParam("limit") Integer size)
    {
        return new TableResponse<>(goodsService.allGoods(page - 1, size), goodsService.count());
    }

    @GetMapping(value = "type")
    public TableResponse<List<Type>> type(@RequestParam("page") Integer page, @RequestParam("limit") Integer size)
    {
        return new TableResponse<>(goodsService.allType(page - 1, size), goodsService.countType());
    }

    @GetMapping(value = "order")
    public TableResponse<List<Order>> order(@RequestParam("page") Integer page, @RequestParam("limit") Integer size)
    {
        return new TableResponse<>(orderService.allOrder(page - 1, size), orderService.count());
    }

    @GetMapping(value = "user")
    public TableResponse<List<User>> user(@RequestParam("page") Integer page, @RequestParam("limit") Integer size)
    {
        return new TableResponse<>(userService.allUser(page - 1, size), userService.count());
    }

    @GetMapping(value = "notice")
    public TableResponse<List<Notice>> notice(@RequestParam("page") Integer page, @RequestParam("limit") Integer size)
    {
        return new TableResponse<>(noticeService.allNotice(page - 1, size), noticeService.count());
    }

    @PostMapping(value = "remove-type")
    public Response<Object> removeType(@RequestParam("id") Integer id)
    {
        return goodsService.removeType(id);
    }

    @PostMapping(value = "remove-notice")
    public Response<Object> removeNotice(@RequestParam("id") Integer id)
    {
        return noticeService.remove(id);
    }

    @PostMapping(value = "add-notice")
    public Response<Object> addNotice(@RequestParam("content") String content)
    {
        return noticeService.add(content);
    }

    @PostMapping(value = "add-type")
    public Response<Object> addType(
            @RequestParam(name = "icon", required = false) String icon,
            @RequestParam("name") String name,
            @RequestParam("parent") Integer parent
    ) {
        return goodsService.addType(parent, name, icon);
    }




}