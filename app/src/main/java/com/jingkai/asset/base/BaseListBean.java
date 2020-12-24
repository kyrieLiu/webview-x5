package com.jingkai.asset.base;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liuyin on 2018/8/2 14:34
 * 加载List  data实体
 */
public class BaseListBean<T> implements Serializable {
    private int code;
    private int count;
    private String message;
    private ArrayList<T> body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<T> getBody() {
        return body;
    }

    public void setBody(ArrayList<T> body) {
        this.body = body;
    }
}
