package muyu.trade.model;

import java.util.List;

public class TableResponse<T> extends Response {
    private Integer count;
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    public TableResponse() {
        this.code = 200;
        this.msg = "";
        this.data = null;
        this.count = 0;
    }
    public TableResponse(Integer code, String msg, List<T> data, Integer count) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.count = count;
    }
    public TableResponse(Response response, Integer count) {
        this.code = response.getCode().equals(200) ? 0 : response.getCode();
        this.msg = response.getMsg();
        this.data = response.getData();
        this.count = count;
    }
}
