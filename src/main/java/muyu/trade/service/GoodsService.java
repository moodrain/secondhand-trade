package muyu.trade.service;

import muyu.trade.entity.Goods;
import muyu.trade.entity.Type;
import muyu.trade.entity.User;
import muyu.trade.model.Response;
import muyu.trade.repository.GoodsRepository;
import muyu.trade.repository.TypeRepository;
import muyu.trade.enumeration.GoodsStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Service
public class GoodsService {
    private GoodsRepository goodsRepository;
    private TypeRepository typeRepository;

    @Autowired
    public GoodsService(GoodsRepository goodsRepository, TypeRepository typeRepository) {
        this.goodsRepository = goodsRepository;
        this.typeRepository = typeRepository;
    }
    public Response<List<Goods>> goods(Integer type, Integer subType, String province, String city, Integer page, Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<Goods> goods = null;
        Type typeObj = new Type();
        typeObj.setId(type);
        Type subTypeObj = new Type();
        subTypeObj.setId(subType);
        if(type != null && subType != null && !province.isEmpty() && !city.isEmpty())
            goods = goodsRepository.findGoodsByProvinceAndCityAndTypeAndSubTypeAndStatusOrderByIdDesc(province, city, typeObj, subTypeObj, 1, pageable);
        else if(type != null && subType != null)
            goods = goodsRepository.findGoodsByTypeAndSubTypeAndStatusOrderByIdDesc(typeObj, subTypeObj, 1, pageable);
        else if(!province.isEmpty() && !city.isEmpty())
            goods = goodsRepository.findGoodsByProvinceAndCityAndStatusOrderByIdDesc(province, city, 1, pageable);
        else if(!province.isEmpty())
            goods = goodsRepository.findGoodsByProvinceAndStatusOrderByIdDesc(province, 1, pageable);
        else
            goods = goodsRepository.findGoodsByStatus(1, pageable);
        return new Response<List<Goods>>(200, "", goods);
    }
    public Response<List<Goods>> allGoods(Integer page, Integer size) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        return new Response<>(200, "", goodsRepository.findAll(pageable).getContent());
    }
    public Response<List<Goods>> myGoods(HttpSession session) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(0, 100, sort);
        return new Response<List<Goods>>(200, "", goodsRepository.findGoodsByUser((User)session.getAttribute("user"), pageable));
    }
    public Response<Object> publish(HttpSession session, String name, Integer price, String img, String province, String city, String district, Integer type, Integer subType, String detail) {
        String pass = null;
        Goods goods = new Goods();
        Type typeObj = typeRepository.findOne(type);
        Type subTypeObj = typeRepository.findOne(subType);
        if(typeObj == null || subTypeObj == null)
            pass = "商品类型不存在";
        else if(name == null || name.isEmpty())
            pass = "名称不能为空";
        else if(price == null)
            pass = "价格不能为空";
        if(pass == null) {
            goods.setUser((User)session.getAttribute("user"));
            goods.setStatus(GoodsStatus.selling.getNum());
            goods.setDate(new Date());
            goods.setName(name);
            goods.setPrice(price);
            goods.setImg(img);
            goods.setProvince(province);
            goods.setCity(city);
            goods.setDistrict(district);
            goods.setType(typeObj);
            goods.setSubType(subTypeObj);
            goods.setDetail(detail);
            goodsRepository.save(goods);
        }
        return pass == null ? new Response<>() : new Response<>(400, pass, null);
    }
    public Response<Object> update(HttpSession session,Integer id, String name, Integer price, String img, String province, String city, String district, Integer type, Integer subType, String detail) {
        User user = (User)session.getAttribute("user");
        Goods goods = goodsRepository.findOne(id);
        Type typeObj = typeRepository.findOne(type);
        Type subTypeObj = typeRepository.findOne(subType);
        String pass = null;
        if(typeObj == null || subTypeObj == null)
            pass = "商品类型不存在";
        else if(name == null || name.isEmpty())
            pass = "名称不能为空";
        else if(price == null)
            pass = "价格不能为空";
        else if(goods == null)
            pass = "商品不存在";
        else if(!goods.getUser().getId().equals(user.getId()))
            pass = "没有权限";
        if(pass == null)
        {
            goods.setName(name);
            goods.setPrice(price);
            if(img != null && !img.isEmpty())
                goods.setImg(img);
            goods.setProvince(province);
            goods.setCity(city);
            goods.setDistrict(district);
            goods.setType(typeObj);
            goods.setSubType(subTypeObj);
            goods.setDetail(detail);
            goodsRepository.save(goods);
        }
        return pass == null ? new Response<>() : new Response<>(400, pass, null);
    }
    public Response<Object> cancel (HttpSession session, Integer id) {
        User user = (User)session.getAttribute("user");
        Goods goods = goodsRepository.findOne(id);
        if(!goods.getUser().getId().equals(user.getId()))
            return new Response<>(400, "没有权限", null);
        goodsRepository.delete(goods);
        return new Response<>();
    }
    public Response<List<Type>> allType(Integer page, Integer size) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        return new Response<List<Type>>(200, "", typeRepository.findAll(pageable).getContent());
    }
    public Response<Object> removeType(Integer id) {
        Type type = typeRepository.getOne(id);
        if(type == null)
            return new Response<>(400, "分类不存在", null);
        typeRepository.delete(type);
        return new Response<>();
    }
    public Response<Object> addType(Integer parent, String name, String icon) {
        if(!parent.equals(0))
            if(typeRepository.findOne(parent) == null)
                return new Response<>(400, "父分类不存在", null);
        Type type = new Type();
        type.setIcon(icon);
        type.setName(name);
        type.setParent(parent);
        typeRepository.save(type);
        return new Response<>();
    }
    public Integer count()
    {
        return Math.toIntExact(goodsRepository.count());
    }
    public Integer countType()
    {
        return Math.toIntExact(typeRepository.count());
    }

}
