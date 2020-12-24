package com.jingkai.asset.function.repair.entity;

/**
 * Created by liuyin on 2019/5/22 16:45
 * Description:
 */
public class FileBean {
    /**
     * name : 微信截图_20190505194609.png
     * id : 337
     * addr : http://pocketbook.document.jingcaiwang.cn/group1/M00/00/33/rBMBLFzPkz6AS8PeAAHvb7Y4VG4863.png
     * url : http://pocketbook.document.jingcaiwang.cn/group1/M00/00/33/rBMBLFzPkz6AS8PeAAHvb7Y4VG4863.png
     */

    private String name;
    private int id;
    private String addr;
    private String url;
    private String localPath;

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
