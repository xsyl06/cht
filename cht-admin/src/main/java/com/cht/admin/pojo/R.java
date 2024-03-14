package com.cht.admin.pojo;

import com.cht.enums.ReturnEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class R<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -2869963291267867983L;
    /**
     * 返回码值
     */
    private String code;
    /**
     * 交易是否成功
     */
    private boolean success;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回对象
     */
    private T data;

    public static R SUCCESS(Object data) {
        R r = SUCCESS();
        r.setData(data);
        return r;
    }

    public static R SUCCESS() {
        R r = new R();
        r.setSuccess(ReturnEnum.SUCCESS.isSuccess());
        r.setMessage(ReturnEnum.SUCCESS.getMessage());
        r.setCode(ReturnEnum.SUCCESS.getCode());
        return r;
    }

    public static R FAIL(){
        R r = new R();
        r.setSuccess(ReturnEnum.FAIL.isSuccess());
        r.setMessage(ReturnEnum.FAIL.getMessage());
        r.setCode(ReturnEnum.FAIL.getCode());
        return r;
    }

    public static R FAIL(String code, boolean success, String message) {
        R r = new R();
        r.setSuccess(success);
        r.setMessage(message);
        r.setCode(code);
        return r;
    }
    public static R FAIL(ReturnEnum returnEnum) {
        R r = new R();
        r.setSuccess(returnEnum.isSuccess());
        r.setMessage(returnEnum.getMessage());
        r.setCode(returnEnum.getCode());
        return r;
    }

    public R() {
    }
    public R(String code, boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
