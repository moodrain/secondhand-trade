package muyu.trade.repository;

import muyu.trade.entity.Order;
import muyu.trade.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer>
{
    List<Order> findOrdersByBuyerOrSeller(User buyer, User seller);
}
