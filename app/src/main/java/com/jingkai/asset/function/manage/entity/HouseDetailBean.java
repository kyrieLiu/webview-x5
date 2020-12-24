package com.jingkai.asset.function.manage.entity;

/**
 * Created by liuyin on 2019/4/19 14:16
 * Description:
 */
public class HouseDetailBean {
    /**
     * assets : {"acreage":"500 平方米","assetCategoryName":"","assetsName":"测试一类资产合同用房产","assetsPropertyCode":"FIRCONTEST001","creator":"张云浩","editor":"张云浩","gmtCreate":"2019-03-27","gmtModified":"2019-03-27","id":3,"paymentStatus":"1000 ㎡/天/元","positionName":"万源商务中心>B1层>强电间","statusName":"空置","useType":1010,"useTypeTitle":"可经营"}
     */

    private AssetsBean assets;

    public AssetsBean getAssets() {
        return assets;
    }

    public void setAssets(AssetsBean assets) {
        this.assets = assets;
    }

    public static class AssetsBean {
        /**
         * acreage : 500 平方米
         * assetCategoryName :
         * assetsName : 测试一类资产合同用房产
         * assetsPropertyCode : FIRCONTEST001
         * creator : 张云浩
         * editor : 张云浩
         * gmtCreate : 2019-03-27
         * gmtModified : 2019-03-27
         * id : 3
         * paymentStatus : 1000 ㎡/天/元
         * positionName : 万源商务中心>B1层>强电间
         * statusName : 空置
         * useType : 1010
         * useTypeTitle : 可经营
         */

        private String acreage;
        private String assetCategoryName;
        private String assetsName;
        private String assetsPropertyCode;
        private String creator;
        private String editor;
        private String gmtCreate;
        private String gmtModified;
        private int id;
        private String paymentStatus;
        private String positionName;
        private String statusName;
        private int useType;
        private String useTypeTitle;

        public String getAcreage() {
            return acreage;
        }

        public void setAcreage(String acreage) {
            this.acreage = acreage;
        }

        public String getAssetCategoryName() {
            return assetCategoryName;
        }

        public void setAssetCategoryName(String assetCategoryName) {
            this.assetCategoryName = assetCategoryName;
        }

        public String getAssetsName() {
            return assetsName;
        }

        public void setAssetsName(String assetsName) {
            this.assetsName = assetsName;
        }

        public String getAssetsPropertyCode() {
            return assetsPropertyCode;
        }

        public void setAssetsPropertyCode(String assetsPropertyCode) {
            this.assetsPropertyCode = assetsPropertyCode;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getEditor() {
            return editor;
        }

        public void setEditor(String editor) {
            this.editor = editor;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getGmtModified() {
            return gmtModified;
        }

        public void setGmtModified(String gmtModified) {
            this.gmtModified = gmtModified;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getPositionName() {
            return positionName;
        }

        public void setPositionName(String positionName) {
            this.positionName = positionName;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public int getUseType() {
            return useType;
        }

        public void setUseType(int useType) {
            this.useType = useType;
        }

        public String getUseTypeTitle() {
            return useTypeTitle;
        }

        public void setUseTypeTitle(String useTypeTitle) {
            this.useTypeTitle = useTypeTitle;
        }
    }
}
