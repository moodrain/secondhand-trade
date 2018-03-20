package muyu.trade.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "\"order\"")
public class Order {

    @Id
    @GeneratedValue
    private Integer id;
    @OneToOne
    private Goods goods;
    @ManyToOne
    private User seller;
    @ManyToOne
    private User buyer;
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    private Integer status;

}
