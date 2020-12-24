package com.jingkai.asset.function.manage.entity;

import java.util.List;

/**
 * Created by liuyin on 2019/4/19 10:33
 * Description:设备设施详情
 */
public class EquipmentDetailBean {

    /**
     * equipment : {"asssetCategoryName":"","brand":"麦克维尔","equipmentCode":"NT-KTJZYCCF-B-17-XFJF-01-0000","equipmentName":"16#空调新风机组","equipmentStatusName":"在用","gmtFirstStartDate":"2014-05-01","gmtRecordDate":"","id":393,"position":"新风机房","purchasePrice":0,"repairDate":"","serviceLife":0,"supplierPhone":""}
     * repairList : [{"creator":"管理员","gmtCreateDateStr":"2019-04-12","id":37,"notes":"hahahahaa","repairTypeName":"维保"}]
     */

    private EquipmentBean equipment;
    private List<RepairListBean> repairList;

    public EquipmentBean getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentBean equipment) {
        this.equipment = equipment;
    }

    public List<RepairListBean> getRepairList() {
        return repairList;
    }

    public void setRepairList(List<RepairListBean> repairList) {
        this.repairList = repairList;
    }

    public static class EquipmentBean {
        /**
         * asssetCategoryName :
         * brand : 麦克维尔
         * equipmentCode : NT-KTJZYCCF-B-17-XFJF-01-0000
         * equipmentName : 16#空调新风机组
         * equipmentStatusName : 在用
         * gmtFirstStartDate : 2014-05-01
         * gmtRecordDate :
         * id : 393
         * position : 新风机房
         * purchasePrice : 0
         * repairDate :
         * serviceLife : 0
         * supplierPhone :
         */

        private String asssetCategoryName;
        private String brand;
        private String equipmentCode;
        private String equipmentName;
        private String equipmentStatusName;
        private String gmtFirstStartDate;
        private String gmtRecordDate;
        private int id;
        private String position;
        private int purchasePrice;
        private String repairDate;
        private int serviceLife;
        private String supplierPhone;
        private String gmtScrappedYears;

        public String getGmtScrappedYears() {
            return gmtScrappedYears;
        }

        public void setGmtScrappedYears(String gmtScrappedYears) {
            this.gmtScrappedYears = gmtScrappedYears;
        }

        public String getAsssetCategoryName() {
            return asssetCategoryName;
        }

        public void setAsssetCategoryName(String asssetCategoryName) {
            this.asssetCategoryName = asssetCategoryName;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getEquipmentCode() {
            return equipmentCode;
        }

        public void setEquipmentCode(String equipmentCode) {
            this.equipmentCode = equipmentCode;
        }

        public String getEquipmentName() {
            return equipmentName;
        }

        public void setEquipmentName(String equipmentName) {
            this.equipmentName = equipmentName;
        }

        public String getEquipmentStatusName() {
            return equipmentStatusName;
        }

        public void setEquipmentStatusName(String equipmentStatusName) {
            this.equipmentStatusName = equipmentStatusName;
        }

        public String getGmtFirstStartDate() {
            return gmtFirstStartDate;
        }

        public void setGmtFirstStartDate(String gmtFirstStartDate) {
            this.gmtFirstStartDate = gmtFirstStartDate;
        }

        public String getGmtRecordDate() {
            return gmtRecordDate;
        }

        public void setGmtRecordDate(String gmtRecordDate) {
            this.gmtRecordDate = gmtRecordDate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public int getPurchasePrice() {
            return purchasePrice;
        }

        public void setPurchasePrice(int purchasePrice) {
            this.purchasePrice = purchasePrice;
        }

        public String getRepairDate() {
            return repairDate;
        }

        public void setRepairDate(String repairDate) {
            this.repairDate = repairDate;
        }

        public int getServiceLife() {
            return serviceLife;
        }

        public void setServiceLife(int serviceLife) {
            this.serviceLife = serviceLife;
        }

        public String getSupplierPhone() {
            return supplierPhone;
        }

        public void setSupplierPhone(String supplierPhone) {
            this.supplierPhone = supplierPhone;
        }
    }

    public static class RepairListBean {
        /**
         * creator : 管理员
         * gmtCreateDateStr : 2019-04-12
         * id : 37
         * notes : hahahahaa
         * repairTypeName : 维保
         */

        private String creator;
        private String gmtCreateDateStr;
        private int id;
        private String notes;
        private String repairTypeName;

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getGmtCreateDateStr() {
            return gmtCreateDateStr;
        }

        public void setGmtCreateDateStr(String gmtCreateDateStr) {
            this.gmtCreateDateStr = gmtCreateDateStr;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getRepairTypeName() {
            return repairTypeName;
        }

        public void setRepairTypeName(String repairTypeName) {
            this.repairTypeName = repairTypeName;
        }
    }
}
