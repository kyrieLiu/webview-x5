package com.jingkai.asset.function.repair.entity;

/**
 * Created by liuyin on 2019/4/9 10:41
 * Description: 审核过程实体
 */
public class RepairAuditProcessBean {
    private String name;
    private String status;
    private String time;

    public RepairAuditProcessBean(String name, String status, String time) {
        this.name = name;
        this.status = status;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
