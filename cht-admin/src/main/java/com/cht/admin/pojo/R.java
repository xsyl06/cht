/*
 * MIT License
 * Copyright 2024-present cht
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.cht.admin.pojo;

import com.cht.enums.ReturnEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
/**
 * @description: 通用返回前端的类
 * @author Wang
 * @date 2024/3/18 9:24
 * @version 1.0
 */
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
