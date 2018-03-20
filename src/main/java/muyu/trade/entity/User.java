package muyu.trade.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String phone;
    private String nick;
    @JsonIgnore
    private String password;
    private String name;
    private Integer sex;
    private Integer age;
    private String province;
    private String city;
    private String district;
    private String mail;
    private String qq;
    private String wx;
    @Lob
    @Column(columnDefinition="LONGTEXT")
    private String avatar;
    @Lob
    @Column(columnDefinition="TEXT")
    private String introduction;
    private Integer isInfoPublic;
    private Integer isHistoryPublic;



    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Lazy
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public Integer getIsInfoPublic() {
        return isInfoPublic;
    }

    public void setIsInfoPublic(Integer isInfoPublic) {
        this.isInfoPublic = isInfoPublic;
    }

    public Integer getIsHistoryPublic() {
        return isHistoryPublic;
    }

    public void setIsHistoryPublic(Integer isHistoryPublic) {
        this.isHistoryPublic = isHistoryPublic;
    }

}
