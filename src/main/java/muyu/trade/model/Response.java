package muyu.trade.model;

import java.util.List;

public class Response<T> {

    protected Integer code;
    protected String msg;
    protected T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    public Response()
    {
        this.code = 200;
        this.msg = "";
        this.data = null;
    }
    public Response(Integer code, String msg, T data)
    {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
