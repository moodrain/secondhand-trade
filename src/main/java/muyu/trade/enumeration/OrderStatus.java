package muyu.trade.enumeration;

public enum OrderStatus {
    waitAgree(        1, "等待卖家同意"),
    waitBothConfirm(  2, "等待双方同意"),
    waitBuyerConfirm( 3, "等待买家确认"),
    waitSellerConfirm(4, "等待卖家确认"),
    finish(           5, "订单完成"),
    sellerNotAgree(   6, "卖家未同意"),
    buyerCancel(      7, "买家取消"),
    sellerCancel(     8, "卖家取消");

    private String info;
    private Integer num;
    OrderStatus(Integer num, String info) {
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
