package muyu.trade.controller;

import muyu.trade.entity.Goods;
import muyu.trade.entity.Type;
import muyu.trade.model.Response;
import muyu.trade.repository.GoodsRepository;
import muyu.trade.repository.TypeRepository;
import muyu.trade.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "api")
public class GoodsController {
    private GoodsService goodsService;
    private GoodsRepository goodsRepository;
    private TypeRepository typeRepository;
    @Autowired
    public GoodsController(GoodsService goodsService, TypeRepository typeRepository, GoodsRepository goodsRepository) {
        this.goodsService = goodsService;
        this.typeRepository = typeRepository;
        this.goodsRepository = goodsRepository;
    }
    @GetMapping(value = "goods")
    public Response<List<Goods>> goods(
            @RequestParam(name = "type", required = false) Integer type,
            @RequestParam(name = "subType", required = false) Integer subType,
            @RequestParam(name = "province", required = false) String province,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) {
        return goodsService.goods(type, subType, province, city, page, size);
    }
    @GetMapping(value = "my-goods")
    public Response<List<Goods>> myGoods(HttpSession session)
    {
        return goodsService.myGoods(session);
    }
    @GetMapping(value = "goods-item")
    public Response<Goods> goodsItem(@RequestParam("id") Integer id) {
        return new Response<Goods>(200, "", goodsRepository.findOne(id));
    }
    @GetMapping(value = "type")
    public Response<List<Type>> types() {
        List<Type> types = typeRepository.findAll();
        return new Response<List<Type>>(200, "", types);
    }
    @PostMapping(value = "publish")
    public Response<Object> publish(HttpSession session,
        @RequestParam("name") String name,
        @RequestParam("price") Integer price,
        @RequestParam("img") String img,
        @RequestParam("province") String province,
        @RequestParam("city") String city,
        @RequestParam("district") String district,
        @RequestParam("type") Integer type,
        @RequestParam("subType") Integer subType,
        @RequestParam("detail") String detail) {
        return goodsService.publish(session, name, price, img, province, city, district, type, subType, detail);
    }
    @PostMapping(value = "update-goods")
    public Response<Object> publish(HttpSession session,
                                    @RequestParam("id") Integer id,
                                    @RequestParam("name") String name,
                                    @RequestParam("price") Integer price,
                                    @RequestParam("img") String img,
                                    @RequestParam("province") String province,
                                    @RequestParam("city") String city,
                                    @RequestParam("district") String district,
                                    @RequestParam("type") Integer type,
                                    @RequestParam("subType") Integer subType,
                                    @RequestParam("detail") String detail) {
        return goodsService.update(session, id, name, price, img, province, city, district, type, subType, detail);
    }
    @PostMapping("cancel-goods")
    public Response<Object> cancel(HttpSession session, @RequestParam("id") Integer id) {
        return goodsService.cancel(session, id);
    }
}