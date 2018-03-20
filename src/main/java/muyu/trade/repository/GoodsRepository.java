package muyu.trade.repository;

import muyu.trade.entity.Goods;
import muyu.trade.entity.Type;
import muyu.trade.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Integer>{
    List<Goods> findGoodsByProvinceAndCityAndTypeAndSubTypeAndStatusOrderByIdDesc(String province, String city, Type type, Type subType, Integer status, Pageable pageable);
    List<Goods> findGoodsByProvinceAndTypeAndSubTypeAndStatusOrderByIdDesc(String province, Type type, Type subType, Integer status, Pageable pageable);
    List<Goods> findGoodsByTypeAndSubTypeAndStatusOrderByIdDesc(Type type, Type subType, Integer status, Pageable pageable);
    List<Goods> findGoodsByProvinceAndCityAndStatusOrderByIdDesc(String province, String city, Integer status, Pageable pageable);
    List<Goods> findGoodsByProvinceAndStatusOrderByIdDesc(String province, Integer status, Pageable pageable);
    List<Goods> findGoodsByStatus(Integer status, Pageable pageable);
    List<Goods> findGoodsByUser(User user, Pageable pageable);
}
