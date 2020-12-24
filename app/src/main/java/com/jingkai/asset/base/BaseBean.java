package com.jingkai.asset.base;

import java.io.Serializable;

/**
 * Created by liuyin on 2018/7/31 14:19
 * @Describe 数据解析基类
 */
public class BaseBean<T>  implements Serializable {
    private static final long serialVersionUID = 1876345352L;
    private int code;
    private String message;
    private T body;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {

        this.body = body;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", body=" + body +
                '}';
    }
}
