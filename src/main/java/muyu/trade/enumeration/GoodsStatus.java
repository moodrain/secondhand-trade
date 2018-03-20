package muyu.trade.enumeration;

public enum GoodsStatus {
    selling(1, "已上架"),
    trading(2, "交易中"),
    sold(3, "已卖出");

    private String info;
    private Integer num;
    GoodsStatus(Integer num, String info)
    {
        this.info = info;
        this.num = num;
    }
    public String toString()
    {
        return info;
    }
    public String getInfo()
    {
        return info;
    }
    public Integer getNum()
    {
        return num;
    }
}
