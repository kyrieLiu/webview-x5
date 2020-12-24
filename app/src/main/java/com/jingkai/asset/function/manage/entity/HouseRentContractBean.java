package com.jingkai.asset.function.manage.entity;

import java.util.List;

/**
 * Created by liuyin on 2019/4/15 11:32
 * Description:
 */
public class HouseRentContractBean {


    /**
     * conBModeList : [{"billingModeTitle":"按日计费","endDate":"2019-11-30","rent":"1.2元/平米/天","startDate":"2019-07-02"}]
     * contractName : 201907020001
     * contractStatusTitle : 执行中
     * customerName : 北京烤鸭有限公司
     * depositType : 押1月付1月
     * gmtEndDateStr : 2019/11/30
     * gmtStartDateStr : 2019/07/02
     * inContractNo : FIR2019070200001T
     * industryTypeName : 医药/医疗/保健
     * outContractNo : 201907020001
     */

    private String contractName;
    private String contractStatusTitle;
    private String customerName;
    private String depositType;
    private String gmtEndDateStr;
    private String gmtStartDateStr;
    private String inContractNo;
    private String industryTypeName;
    private String outContractNo;
    private List<ConBModeListBean> conBModeList;

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractStatusTitle() {
        return contractStatusTitle;
    }

    public void setContractStatusTitle(String contractStatusTitle) {
        this.contractStatusTitle = contractStatusTitle;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDepositType() {
        return depositType;
    }

    public void setDepositType(String depositType) {
        this.depositType = depositType;
    }

    public String getGmtEndDateStr() {
        return gmtEndDateStr;
    }

    public void setGmtEndDateStr(String gmtEndDateStr) {
        this.gmtEndDateStr = gmtEndDateStr;
    }

    public String getGmtStartDateStr() {
        return gmtStartDateStr;
    }

    public void setGmtStartDateStr(String gmtStartDateStr) {
        this.gmtStartDateStr = gmtStartDateStr;
    }

    public String getInContractNo() {
        return inContractNo;
    }

    public void setInContractNo(String inContractNo) {
        this.inContractNo = inContractNo;
    }

    public String getIndustryTypeName() {
        return industryTypeName;
    }

    public void setIndustryTypeName(String industryTypeName) {
        this.industryTypeName = industryTypeName;
    }

    public String getOutContractNo() {
        return outContractNo;
    }

    public void setOutContractNo(String outContractNo) {
        this.outContractNo = outContractNo;
    }

    public List<ConBModeListBean> getConBModeList() {
        return conBModeList;
    }

    public void setConBModeList(List<ConBModeListBean> conBModeList) {
        this.conBModeList = conBModeList;
    }

    public static class ConBModeListBean {
        /**
         * billingModeTitle : 按日计费
         * endDate : 2019-11-30
         * rent : 1.2元/平米/天
         * startDate : 2019-07-02
         */

        private String billingModeTitle;
        private String endDate;
        private String rent;
        private String startDate;

        public String getBillingModeTitle() {
            return billingModeTitle;
        }

        public void setBillingModeTitle(String billingModeTitle) {
            this.billingModeTitle = billingModeTitle;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getRent() {
            return rent;
        }

        public void setRent(String rent) {
            this.rent = rent;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }
    }
}
