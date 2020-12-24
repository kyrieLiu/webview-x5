package com.jingkai.asset.function.repair.entity;

/**
 * Created by liuyin on 2019/4/8 19:46
 * Description:
 */
public class AuditStatusBean {
    private int supervisoryStatus;
    private String statusName;
    private int haveDone;
    private boolean isLastDone;//是否为执行的最后一个节点

    public int getSupervisoryStatus() {
        return supervisoryStatus;
    }

    public void setSupervisoryStatus(int supervisoryStatus) {
        this.supervisoryStatus = supervisoryStatus;
    }

    public boolean isLastDone() {
        return isLastDone;
    }

    public void setLastDone(boolean lastDone) {
        isLastDone = lastDone;
    }

    public AuditStatusBean() {
    }

    public AuditStatusBean(String statusName) {
        this.statusName = statusName;
    }

    public int getHaveDone() {
        return haveDone;
    }

    public void setHaveDone(int haveDone) {
        this.haveDone = haveDone;
    }


    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public boolean equals(Object obj) {
        AuditStatusBean bean = (AuditStatusBean) obj;
        if (this.statusName.equals(bean.getStatusName())) {
            return true;
        }
//        if (obj instanceof String) {
//            String statusName = (String) obj;
//            if (this.statusName.equals(statusName)) {
//                return true;
//            }
//        }
        return super.equals(obj);
    }
}
