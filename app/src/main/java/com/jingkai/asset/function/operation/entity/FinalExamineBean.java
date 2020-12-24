package com.jingkai.asset.function.operation.entity;

import java.util.List;

/**
 * Created by liuyin on 2019/4/16 11:37
 * Description:修缮终审列表实体
 */
public class FinalExamineBean {
    private List<FinalExamineBean.ItemsBean> items;
    private int total;

    public List<FinalExamineBean.ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<FinalExamineBean.ItemsBean> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class ItemsBean {


        /**
         * finalReviewFailed : 80
         * finalReviewFailedStatus : 终审不通过
         * gmtStart : 2019/04/01 08:00:00
         * id : 201
         * idAcParkTitle : 博大万源公寓
         * name : 2019年第二季度修缮计划
         * nameRbacDepartment : 亦庄置业
         */

        private int finalReviewFailed;
        private String finalReviewFailedStatus;
        private String gmtStart;
        private int id;
        private String idAcParkTitle;
        private String name;
        private String nameRbacDepartment;
        private int isRead;

        public int getIsRead() {
            return isRead;
        }

        public void setIsRead(int isRead) {
            this.isRead = isRead;
        }

        public int getFinalReviewFailed() {
            return finalReviewFailed;
        }

        public void setFinalReviewFailed(int finalReviewFailed) {
            this.finalReviewFailed = finalReviewFailed;
        }

        public String getFinalReviewFailedStatus() {
            return finalReviewFailedStatus;
        }

        public void setFinalReviewFailedStatus(String finalReviewFailedStatus) {
            this.finalReviewFailedStatus = finalReviewFailedStatus;
        }

        public String getGmtStart() {
            return gmtStart;
        }

        public void setGmtStart(String gmtStart) {
            this.gmtStart = gmtStart;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNameRbacDepartment() {
            return nameRbacDepartment;
        }

        public void setNameRbacDepartment(String nameRbacDepartment) {
            this.nameRbacDepartment = nameRbacDepartment;
        }
    }
}
