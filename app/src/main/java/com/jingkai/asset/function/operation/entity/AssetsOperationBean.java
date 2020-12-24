package com.jingkai.asset.function.operation.entity;

/**
 * Created by liuyin on 2019/4/15 15:33
 * Description:
 */
public class AssetsOperationBean {
    private String name;
    private int icon;
    private Class<?> zclass;//需要跳转的Class
    private int intentType;//跳转数据
    private String data;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getIntentType() {
        return intentType;
    }

    public void setIntentType(int intentType) {
        this.intentType = intentType;
    }

    public Class<?> getZclass() {
        return zclass;
    }

    public void setZclass(Class<?> zclass) {
        this.zclass = zclass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
