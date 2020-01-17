package muyu.trade.controller;

import muyu.trade.entity.Order;
import muyu.trade.entity.User;
import muyu.trade.model.Response;
import muyu.trade.repository.OrderRepository;
import muyu.trade.service.OrderService;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(path = "api")
public class OrderController {
    private OrderService orderService;
    private OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }
    @PostMapping(value = "buy")
    public Response<Object> buy(@RequestParam("id") Integer id, HttpSession session) {
        return orderService.buy(id, session);
    }
    @GetMapping(value = "order")
    public Response<List<Order>> order(HttpSession session) {
        return new Response<List<Order>>(200, "", orderRepository.findOrdersByBuyerOrSeller((User)session.getAttribute("user"), (User)session.getAttribute("user")));
    }
    @PostMapping(value = "order-cancel")
    public Response<Object> orderCancel(@RequestParam("id") Integer id, HttpSession session) {
        return orderService.orderCancel(id, session);
    }
    @PostMapping(value = "order-next")
    public Response<Object> orderNext(@RequestParam("id") Integer id, HttpSession session) {
        return orderService.orderNext(id, session);
    }
}