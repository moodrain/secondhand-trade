package muyu.trade.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Notice {

    @Id
    @GeneratedValue
    private Integer id;
    @Lob
    @Column(columnDefinition="TEXT")
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
