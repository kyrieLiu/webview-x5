package com.jingkai.asset.function.operation.entity;

import java.util.List;

/**
 * 供应商实体
 * created by YQ on 2019-05-23 15:33
 */
public class SupplierBean {


    private List<CompanyListBean> companyList;

    public List<CompanyListBean> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<CompanyListBean> companyList) {
        this.companyList = companyList;
    }

    public static class CompanyListBean {
        /**
         * departName : 经营单位
         * pid : 1
         * professionalList : []
         */

        private String departName;
        private int pid;
        private List<ProfessionalListBean> professionalList;

        public String getDepartName() {
            return departName;
        }

        public void setDepartName(String departName) {
            this.departName = departName;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public List<ProfessionalListBean> getProfessionalList() {
            return professionalList;
        }

        public void setProfessionalList(List<ProfessionalListBean> professionalList) {
            this.professionalList = professionalList;
        }

        public static class ProfessionalListBean{

            /**
             * count : 8
             * pid : 2
             * professionalFieldTitle : 互联网
             * supplierCompanyList : [{"contactPhone":"13829723805","nameRbacDepartment":"资产管理部","pid":3,"professionalField":"156","professionalFieldTitle":"互联网","supplierName":"1"},{"contactPhone":"13412351235","nameRbacDepartment":"北京经开股份有限公司","pid":3,"professionalField":"156","professionalFieldTitle":"互联网","supplierName":"发生发射点发"},{"contactPhone":"13412351235","nameRbacDepartment":"北京经开股份有限公司","pid":3,"professionalField":"156","professionalFieldTitle":"互联网","supplierName":"发生发射点发"},{"contactPhone":"13829723805","nameRbacDepartment":"资产管理部","pid":3,"professionalField":"156","professionalFieldTitle":"互联网","supplierName":"1"},{"contactPhone":"13899999999","nameRbacDepartment":"北京经开股份有限公司","pid":3,"professionalField":"156","professionalFieldTitle":"互联网","supplierName":"我是供应商"},{"contactPhone":"1320000000005","nameRbacDepartment":"北京经开股份有限公司","pid":3,"professionalField":"156","professionalFieldTitle":"互联网","supplierName":"q"},{"contactPhone":"13812345678910","nameRbacDepartment":"北京经开股份有限公司","pid":3,"professionalField":"156","professionalFieldTitle":"互联网","supplierName":"11"},{"contactPhone":"13812345678910","nameRbacDepartment":"北京经开股份有限公司","pid":3,"professionalField":"156","professionalFieldTitle":"互联网","supplierName":"11"}]
             */

            private int count;
            private int pid;
            private String professionalFieldTitle;
            private List<SupplierCompanyListBean> supplierCompanyList;

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

            public String getProfessionalFieldTitle() {
                return professionalFieldTitle;
            }

            public void setProfessionalFieldTitle(String professionalFieldTitle) {
                this.professionalFieldTitle = professionalFieldTitle;
            }

            public List<SupplierCompanyListBean> getSupplierCompanyList() {
                return supplierCompanyList;
            }

            public void setSupplierCompanyList(List<SupplierCompanyListBean> supplierCompanyList) {
                this.supplierCompanyList = supplierCompanyList;
            }

            public static class SupplierCompanyListBean {
                /**
                 * contactPhone : 13829723805
                 * nameRbacDepartment : 资产管理部
                 * pid : 3
                 * professionalField : 156
                 * professionalFieldTitle : 互联网
                 * supplierName : 1
                 */

                private String contactPhone;
                private String nameRbacDepartment;
                private int pid;
                private String professionalField;
                private String professionalFieldTitle;
                private String supplierName;

                public String getContactPhone() {
                    return contactPhone;
                }

                public void setContactPhone(String contactPhone) {
                    this.contactPhone = contactPhone;
                }

                public String getNameRbacDepartment() {
                    return nameRbacDepartment;
                }

                public void setNameRbacDepartment(String nameRbacDepartment) {
                    this.nameRbacDepartment = nameRbacDepartment;
                }

                public int getPid() {
                    return pid;
                }

                public void setPid(int pid) {
                    this.pid = pid;
                }

                public String getProfessionalField() {
                    return professionalField;
                }

                public void setProfessionalField(String professionalField) {
                    this.professionalField = professionalField;
                }

                public String getProfessionalFieldTitle() {
                    return professionalFieldTitle;
                }

                public void setProfessionalFieldTitle(String professionalFieldTitle) {
                    this.professionalFieldTitle = professionalFieldTitle;
                }

                public String getSupplierName() {
                    return supplierName;
                }

                public void setSupplierName(String supplierName) {
                    this.supplierName = supplierName;
                }
            }
        }

    }


}
