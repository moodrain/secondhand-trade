package muyu.trade.service;

import muyu.trade.entity.Goods;
import muyu.trade.entity.Order;
import muyu.trade.entity.User;
import muyu.trade.enumeration.GoodsStatus;
import muyu.trade.enumeration.OrderStatus;
import muyu.trade.model.Response;
import muyu.trade.repository.GoodsRepository;
import muyu.trade.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private GoodsRepository goodsRepository;
    private MessageService messageService;

    public OrderService(OrderRepository orderRepository, GoodsRepository goodsRepository, MessageService messageService) {
        this.orderRepository = orderRepository;
        this.goodsRepository = goodsRepository;
        this.messageService = messageService;
    }
    @Transactional
    public Response<Object> buy(Integer id, HttpSession session) {
        Goods goods = goodsRepository.findOne(id);
        if(goods == null)
            return new Response<>(400, "商品不存在", null);
        else if(!goods.getStatus().equals(OrderStatus.waitAgree.getNum()))
            return new Response<>(400, "该商品已经被购买", null);
        else if(goods.getUser().getId().equals(((User)session.getAttribute("user")).getId()))
            return new Response<>(400, "不能购买自己发布的商品", null);
        goods.setStatus(GoodsStatus.trading.getNum());
        goodsRepository.save(goods);
        User user = (User)session.getAttribute("user");
        Order order = new Order();
        order.setDate(new Date());
        order.setBuyer(user);
        order.setGoods(goods);
        order.setSeller(goods.getUser());
        order.setStatus(OrderStatus.waitAgree.getNum());
        orderRepository.save(order);
        messageService.sendNoticeMessage(order.getBuyer().getId(), order.getSeller().getId(), "下单了你上架的商品：" + goods.getName());
        messageService.sendNoticeMessage(0, user.getId(), "你下单了商品：" + goods.getName());
        return new Response<>();
    }
    @Transactional
    public Response<Object> orderCancel(Integer id, HttpSession session) {
        Order order = orderRepository.findOne(id);
        if(order == null)
            return new Response<>(400, "订单不存在", null);
        User user = (User)session.getAttribute("user");
        if(user.getId().equals(order.getBuyer().getId())) {
            if(order.getStatus().equals(OrderStatus.waitAgree.getNum()) || order.getStatus().equals(OrderStatus.waitBothConfirm.getNum()) || order.getStatus().equals(OrderStatus.waitBuyerConfirm.getNum()) || order.getStatus().equals(OrderStatus.waitSellerConfirm.getNum())) {
                Goods goods = order.getGoods();
                goods.setStatus(OrderStatus.waitAgree.getNum());
                order.setStatus(OrderStatus.buyerCancel.getNum());
                goodsRepository.save(goods);
                orderRepository.save(order);
                messageService.sendNoticeMessage(order.getBuyer().getId(), order.getSeller().getId(), "取消了购买：" + goods.getName() + " 的订单");
                messageService.sendNoticeMessage(0, user.getId(), "你取消了购买：" + goods.getName() + " 的订单");
                return new Response<>();
            }
            else
                return new Response<>(400, "订单状态不可取消", null);
        }
        else if(user.getId().equals(order.getSeller().getId())) {
            if(order.getStatus().equals(OrderStatus.waitAgree.getNum())) {
                Goods goods = order.getGoods();
                goods.setStatus(OrderStatus.waitAgree.getNum());
                order.setStatus(OrderStatus.sellerNotAgree.getNum());
                goodsRepository.save(goods);
                orderRepository.save(order);
                messageService.sendNoticeMessage(order.getSeller().getId(), order.getBuyer().getId(), "不同意你购买：" + goods.getName());
                messageService.sendNoticeMessage(0, user.getId(), "你不同意 " + order.getBuyer().getNick() + " 购买 " + goods.getName() + " 的订单");
                return new Response<>();
            }
            else if(order.getStatus().equals(OrderStatus.waitBothConfirm.getNum()) || order.getStatus().equals(OrderStatus.waitBuyerConfirm.getNum()) || order.getStatus().equals(OrderStatus.waitSellerConfirm.getNum())) {
                Goods goods = order.getGoods();
                goods.setStatus(OrderStatus.waitAgree.getNum());
                order.setStatus(OrderStatus.sellerCancel.getNum());
                goodsRepository.save(goods);
                orderRepository.save(order);
                messageService.sendNoticeMessage(order.getSeller().getId(), order.getBuyer().getId(), "取消了卖出：" + goods.getName() + " 的订单");
                messageService.sendNoticeMessage(0, user.getId(), "你取消了卖出：" + goods.getName() + " 的订单");
                return new Response<>();
            }
            else
                return new Response<>(400, "订单状态不可取消", null);
        }
        else
            return new Response<>(400, "没有权限", null);
    }
    @Transactional
    public Response<Object> orderNext(Integer id, HttpSession session) {
        User user  = (User)session.getAttribute("user");
        Order order = orderRepository.findOne(id);
        if(order.getBuyer().getId().equals(user.getId())) {
            if(order.getStatus().equals(OrderStatus.waitBothConfirm.getNum())) {
                order.setStatus(OrderStatus.waitSellerConfirm.getNum());
                orderRepository.save(order);
                messageService.sendNoticeMessage(order.getBuyer().getId(), order.getSeller().getId(), "确认了购买：" + order.getGoods().getName() + " 的订单，等待你的确认");
                messageService.sendNoticeMessage(0, user.getId(), "你确认了购买：" + order.getGoods().getName() + "的订单");
                return new Response<>();
            }
            else if(order.getStatus().equals(OrderStatus.waitBuyerConfirm.getNum())) {
                order.setStatus(OrderStatus.finish.getNum());
                Goods goods = order.getGoods();
                goods.setStatus(GoodsStatus.sold.getNum());
                goodsRepository.save(goods);
                orderRepository.save(order);
                messageService.sendNoticeMessage(order.getBuyer().getId(), order.getSeller().getId(), "确认了购买：" + order.getGoods().getName() + " 的订单，交易完成");
                messageService.sendNoticeMessage(0, user.getId(), "你确认了购买：" + order.getGoods().getName() + "的订单，交易完成");
                return new Response<>();
            }
            else
                return new Response<>(400, "订单状态不可确认", null);
        }
        else if(order.getSeller().getId().equals(user.getId())) {
            if(order.getStatus().equals(OrderStatus.waitAgree.getNum())) {
                order.setStatus(OrderStatus.waitBothConfirm.getNum());
                orderRepository.save(order);
                messageService.sendNoticeMessage(order.getSeller().getId(), order.getBuyer().getId(), "同意了你购买：" + order.getGoods().getName() + "的订单");
                messageService.sendNoticeMessage(0, user.getId(), "你同意了" + order.getBuyer().getNick() + "购买：" + order.getGoods().getName() + "的订单");
                return new Response<>();
            }
            else if(order.getStatus().equals(OrderStatus.waitBothConfirm.getNum())) {
                order.setStatus(OrderStatus.waitBuyerConfirm.getNum());
                orderRepository.save(order);
                messageService.sendNoticeMessage(order.getSeller().getId(), order.getBuyer().getId(), "确认了卖出：" + order.getGoods().getName() + " 的订单，等待你的确认");
                messageService.sendNoticeMessage(0, user.getId(), "你确认了卖出：" + order.getGoods().getName() + " 的订单");
                return new Response<>();
            }
            else if(order.getStatus().equals(OrderStatus.waitSellerConfirm.getNum())) {
                order.setStatus(OrderStatus.finish.getNum());
                Goods goods = order.getGoods();
                goods.setStatus(GoodsStatus.sold.getNum());
                goodsRepository.save(goods);
                orderRepository.save(order);
                messageService.sendNoticeMessage(order.getSeller().getId(), order.getBuyer().getId(), "确认了卖出：" + order.getGoods().getName() + " 的订单，交易完成");
                messageService.sendNoticeMessage(0, user.getId(), "你确认了卖出：" + order.getGoods().getName() + " 的订单， 交易完成");
                return new Response<>();
            }
            else
                return new Response<>(400, "订单状态不可确认", null);
        }
        else
            return new Response<>(400, "没有权限", null);
    }
    public Response<List<Order>> allOrder(Integer page, Integer size) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        return new Response<List<Order>>(200, "", orderRepository.findAll(pageable).getContent());
    }
    public Integer count()
    {
        return Math.toIntExact(orderRepository.count());
    }
}
