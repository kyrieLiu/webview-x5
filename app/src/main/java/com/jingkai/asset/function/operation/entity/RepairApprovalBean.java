package com.jingkai.asset.function.operation.entity;

import java.util.List;

/**
 * Created by zhangyang
 * Date: 2019/8/28
 * Description: 修缮审批实体
 */
public class RepairApprovalBean {


    /**
     * code : 0
     * body : {"items":[{"repairPlanStatus":"储备中","isUrgent":0,"nameRbacDepartment":"亦庄置业","repairPlanStatusId":5,"name":"222","isRead":0,"id":214,"idAcParkTitle":"经海产业园","gmtStart":"2019/06/27 08:00:00"}],"total":3}
     * message : success
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
         * repairPlanStatus : 储备中
         * isUrgent : 0
         * nameRbacDepartment : 亦庄置业
         * repairPlanStatusId : 5
         * name : 222
         * isRead : 0
         * id : 214
         * idAcParkTitle : 经海产业园
         * gmtStart : 2019/06/27 08:00:00
         */

        private String repairPlanStatus;
        private int isUrgent;
        private String nameRbacDepartment;
        private int repairPlanStatusId;
        private String name;
        private int isRead;
        private int id;
        private String idAcParkTitle;
        private String gmtStart;

        public String getRepairPlanStatus() {
            return repairPlanStatus;
        }

        public void setRepairPlanStatus(String repairPlanStatus) {
            this.repairPlanStatus = repairPlanStatus;
        }

        public int getIsUrgent() {
            return isUrgent;
        }

        public void setIsUrgent(int isUrgent) {
            this.isUrgent = isUrgent;
        }

        public String getNameRbacDepartment() {
            return nameRbacDepartment;
        }

        public void setNameRbacDepartment(String nameRbacDepartment) {
            this.nameRbacDepartment = nameRbacDepartment;
        }

        public int getRepairPlanStatusId() {
            return repairPlanStatusId;
        }

        public void setRepairPlanStatusId(int repairPlanStatusId) {
            this.repairPlanStatusId = repairPlanStatusId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIsRead() {
            return isRead;
        }

        public void setIsRead(int isRead) {
            this.isRead = isRead;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIdAcParkTitle() {
            return idAcParkTitle;
        }

        public void setIdAcParkTitle(String idAcParkTitle) {
            this.idAcParkTitle = idAcParkTitle;
        }

        public String getGmtStart() {
            return gmtStart;
        }

        public void setGmtStart(String gmtStart) {
            this.gmtStart = gmtStart;
        }
    }

}
