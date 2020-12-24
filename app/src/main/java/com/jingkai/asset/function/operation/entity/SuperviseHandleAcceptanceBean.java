package com.jingkai.asset.function.operation.entity;

import java.util.List;

/**
 * Created by liuyin on 2019/4/16 14:28
 * Description:
 */
public class SuperviseHandleAcceptanceBean {
    /**
     * items : [{"nameAcPark":"隆盛大厦","nameRbacDepartment":"亦庄置业","chargeInfo":"发多少","supervisoryStatus":30,"supervisoryCode":"DC2019051500021","missionName":"给顿饭","id":99,"supervisoryStatusTitle":"待验收"},{"nameAcPark":"万源商务中心","nameRbacDepartment":"亦庄置业","chargeInfo":"大","supervisoryStatus":20,"supervisoryCode":"DC2019051500020","missionName":"发送到","id":98,"supervisoryStatusTitle":"待反馈"},{"nameAcPark":"永康公寓","nameRbacDepartment":"亦庄置业","chargeInfo":"crn","supervisoryStatus":20,"supervisoryCode":"DC2019051500019","missionName":"而非我","id":97,"supervisoryStatusTitle":"待反馈"}]
     * total : 3
     */

    private int total;
    private List<ItemsBean> items;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * nameAcPark : 隆盛大厦
         * nameRbacDepartment : 亦庄置业
         * chargeInfo : 发多少
         * supervisoryStatus : 30
         * supervisoryCode : DC2019051500021
         * missionName : 给顿饭
         * id : 99
         * supervisoryStatusTitle : 待验收
         */

        private String nameAcPark;
        private String nameRbacDepartment;
        private String chargeInfo;
        private int supervisoryStatus;
        private String supervisoryCode;
        private String missionName;
        private int id;
        private String supervisoryStatusTitle;
        private String isRead;

        public String getIsRead() {
            return isRead;
        }

        public void setIsRead(String isRead) {
            this.isRead = isRead;
        }

        public String getNameAcPark() {
            return nameAcPark;
        }

        public void setNameAcPark(String nameAcPark) {
            this.nameAcPark = nameAcPark;
        }

        public String getNameRbacDepartment() {
            return nameRbacDepartment;
        }

        public void setNameRbacDepartment(String nameRbacDepartment) {
            this.nameRbacDepartment = nameRbacDepartment;
        }

        public String getChargeInfo() {
            return chargeInfo;
        }

        public void setChargeInfo(String chargeInfo) {
            this.chargeInfo = chargeInfo;
        }

        public int getSupervisoryStatus() {
            return supervisoryStatus;
        }

        public void setSupervisoryStatus(int supervisoryStatus) {
            this.supervisoryStatus = supervisoryStatus;
        }

        public String getSupervisoryCode() {
            return supervisoryCode;
        }

        public void setSupervisoryCode(String supervisoryCode) {
            this.supervisoryCode = supervisoryCode;
        }

        public String getMissionName() {
            return missionName;
        }

        public void setMissionName(String missionName) {
            this.missionName = missionName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSupervisoryStatusTitle() {
            return supervisoryStatusTitle;
        }

        public void setSupervisoryStatusTitle(String supervisoryStatusTitle) {
            this.supervisoryStatusTitle = supervisoryStatusTitle;
        }
    }

}
