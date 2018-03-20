package muyu.trade.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Goods
{
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    @ManyToOne
    private User user;
    private Integer price;
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date date;
    private Integer status;
    private String province;
    private String city;
    private String district;
    @Lob
    @Column(columnDefinition="TEXT")
    private String detail;
    @Lob
    @Column(columnDefinition="LONGTEXT")
    private String img;
    @ManyToOne
    private Type type;
    @ManyToOne
    private Type subType;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getSubType() {
        return subType;
    }

    public void setSubType(Type subType) {
        this.subType = subType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
