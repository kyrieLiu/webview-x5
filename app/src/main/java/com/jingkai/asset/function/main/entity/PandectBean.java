package com.jingkai.asset.function.main.entity;

/**
 * Created by liuyin on 2019/2/28 21:02
 * @Describe 发现列表条目
 */
public class PandectBean {
    private String title;
    private String key;
    private String time;
    private int image;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
