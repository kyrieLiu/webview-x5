package com.jingkai.asset.function.operation.entity;

import java.util.List;

/**
 * created by YQ on 2019-05-23 15:40
 */
public class ProfessionalListBean {

    /**
     * professionalFieldTitle : 互联网
     * count : 8
     * supplierCompanyList : [{"supplierName":"1","professionalField":"156","nameRbacDepartment":"资产管理部","professionalFieldTitle":"互联网","pid":3,"contactPhone":"13829723805"},{"supplierName":"发生发射点发","professionalField":"156","nameRbacDepartment":"北京经开股份有限公司","professionalFieldTitle":"互联网","pid":3,"contactPhone":"13412351235"},{"supplierName":"发生发射点发","professionalField":"156","nameRbacDepartment":"北京经开股份有限公司","professionalFieldTitle":"互联网","pid":3,"contactPhone":"13412351235"},{"supplierName":"1","professionalField":"156","nameRbacDepartment":"资产管理部","professionalFieldTitle":"互联网","pid":3,"contactPhone":"13829723805"},{"supplierName":"我是供应商","professionalField":"156","nameRbacDepartment":"北京经开股份有限公司","professionalFieldTitle":"互联网","pid":3,"contactPhone":"13899999999"},{"supplierName":"q","professionalField":"156","nameRbacDepartment":"北京经开股份有限公司","professionalFieldTitle":"互联网","pid":3,"contactPhone":"1320000000005"},{"supplierName":"11","professionalField":"156","nameRbacDepartment":"北京经开股份有限公司","professionalFieldTitle":"互联网","pid":3,"contactPhone":"13812345678910"},{"supplierName":"11","professionalField":"156","nameRbacDepartment":"北京经开股份有限公司","professionalFieldTitle":"互联网","pid":3,"contactPhone":"13812345678910"}]
     * pid : 2
     */

    private String professionalFieldTitle;
    private int count;
    private int pid;
    private List<SupplierCompanyListBean> supplierCompanyList;

    public String getProfessionalFieldTitle() {
        return professionalFieldTitle;
    }

    public void setProfessionalFieldTitle(String professionalFieldTitle) {
        this.professionalFieldTitle = professionalFieldTitle;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public List<SupplierCompanyListBean> getSupplierCompanyList() {
        return supplierCompanyList;
    }

    public void setSupplierCompanyList(List<SupplierCompanyListBean> supplierCompanyList) {
        this.supplierCompanyList = supplierCompanyList;
    }

    public static class SupplierCompanyListBean {
        /**
         * supplierName : 1
         * professionalField : 156
         * nameRbacDepartment : 资产管理部
         * professionalFieldTitle : 互联网
         * pid : 3
         * contactPhone : 13829723805
         */

        private String supplierName;
        private String professionalField;
        private String nameRbacDepartment;
        private String professionalFieldTitle;
        private int pid;
        private String contactPhone;

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }

        public String getProfessionalField() {
            return professionalField;
        }

        public void setProfessionalField(String professionalField) {
            this.professionalField = professionalField;
        }

        public String getNameRbacDepartment() {
            return nameRbacDepartment;
        }

        public void setNameRbacDepartment(String nameRbacDepartment) {
            this.nameRbacDepartment = nameRbacDepartment;
        }

        public String getProfessionalFieldTitle() {
            return professionalFieldTitle;
        }

        public void setProfessionalFieldTitle(String professionalFieldTitle) {
            this.professionalFieldTitle = professionalFieldTitle;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }
    }
}
