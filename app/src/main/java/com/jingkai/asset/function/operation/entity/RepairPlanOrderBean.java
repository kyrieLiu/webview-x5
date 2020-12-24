package com.jingkai.asset.function.operation.entity;

import java.util.List;

/**
 * Created by liuyin on 2019/4/16 9:42
 * Description:修缮计划单实体
 */
public class RepairPlanOrderBean {

    private List<ItemsBean> items;
    private int total;

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class ItemsBean{


        /**
         * nameRbacDepartment : 亦庄置业
         * repairStatusTitle : 待报审
         * name : 测试2
         * id : 46
         * idAcParkTitle : 博大万源公寓
         * gmtStart : 2019/04/25 00:00:00
         */

        private String nameRbacDepartment;
        private String repairStatusTitle;
        private String name;
        private int id;
        private String idAcParkTitle;
        private String gmtStart;

        public String getNameRbacDepartment() {
            return nameRbacDepartment;
        }

        public void setNameRbacDepartment(String nameRbacDepartment) {
            this.nameRbacDepartment = nameRbacDepartment;
        }

        public String getRepairStatusTitle() {
            return repairStatusTitle;
        }

        public void setRepairStatusTitle(String repairStatusTitle) {
            this.repairStatusTitle = repairStatusTitle;
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
