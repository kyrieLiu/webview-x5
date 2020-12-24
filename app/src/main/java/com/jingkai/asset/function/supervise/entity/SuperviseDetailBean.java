package com.jingkai.asset.function.supervise.entity;

import com.jingkai.asset.function.repair.entity.FileBean;

import java.util.List;

/**
 * Created by liuyin on 2019/5/22 15:43
 * Description:
 */
public class SuperviseDetailBean {
    /**
     * acceptanceOpinion : 完成验收
     * acceptor : {"id":153,"id_ac_supervisory":51,"persontype":1050,"rbac_user_id":153,"rbac_user_name":"资产管理部经理"}
     * action : [{"creator":"资产管理部经理","files":[],"gmtCreate":"2019-05-08 16:51:34","id":212,"notes":"完成","statusChange":"新建提交","supervisoryAction":20,"supervisoryId":51},{"creator":"工程管理专员","files":[],"gmtCreate":"2019-05-08 16:52:59","id":214,"notes":"验收完成","statusChange":"反馈提交","supervisoryAction":40,"supervisoryId":51},{"creator":"资产管理部经理","files":[],"gmtCreate":"2019-05-08 16:53:46","id":215,"notes":"完成验收","statusChange":"验收通过","supervisoryAction":50,"supervisoryId":51}]
     * chargeInfo : 孙哲
     * codeRbacDepartment : .1.1
     * completeTime : 2019-06-29T00:00:00.000+0000
     * creator : 资产管理部经理
     * feedBackFileList : []
     * feedbackContent : 验收完成
     * gmtCreate : 1557305494773
     * id : 51
     * idRbacDepartment : 4
     * missionName : 督查督办单
     * nameAcAssetCategory : 一类资产
     * nameAcAssetPosition : 亦城财富中心>亦城财富中心3号楼>5
     * nameAcAssetsProperty : 504
     * nameAcEquipment :
     * nameAcPark : 博大万源公寓
     * nameRbacDepartment : 亦庄置业
     * notes : 完成
     * orderSourceTitle : 派工工单
     * receiver : {"id":152,"id_ac_supervisory":51,"persontype":2040,"rbac_user_id":192,"rbac_user_name":"工程管理专员"}
     * sort : 1557305494773
     * supervisoryCode : DC2019050800003
     * supervisoryFileList : []
     * supervisoryStatus : 40
     * supervisoryStatusTitle : 已验收
     */

    private String acceptanceOpinion;
    private AcceptorBean acceptor;
    private String chargeInfo;
    private String codeRbacDepartment;
    private long completeTime;
    private String creator;
    private String feedbackContent;
    private long gmtCreate;
    private int id;
    private int idRbacDepartment;
    private String missionName;
    private String nameAcAssetCategory;
    private String nameAcAssetPosition;
    private String nameAcAssetsProperty;
    private String nameAcEquipment;
    private String nameAcPark;
    private String nameRbacDepartment;
    private String notes;
    private String orderSourceTitle;
    private ReceiverBean receiver;
    private long sort;
    private String supervisoryCode;
    private int supervisoryStatus;
    private String supervisoryStatusTitle;
    private int menuCheck;//验收按钮权限：0 没有，1 有
    private int menuBack;//反馈人：0 不是，1 是
    private List<ActionBean> action;
    private List<FileBean> feedBackFileList;
    private List<FileBean> supervisoryFileList;
    private PrincipalBean principal;

    public int getMenuBack() {
        return menuBack;
    }

    public void setMenuBack(int menuBack) {
        this.menuBack = menuBack;
    }

    public PrincipalBean getPrincipal() {
        return principal;
    }

    public void setPrincipal(PrincipalBean principal) {
        this.principal = principal;
    }

    public int getMenuCheck() {
        return menuCheck;
    }

    public void setMenuCheck(int menuCheck) {
        this.menuCheck = menuCheck;
    }

    public String getAcceptanceOpinion() {
        return acceptanceOpinion;
    }

    public void setAcceptanceOpinion(String acceptanceOpinion) {
        this.acceptanceOpinion = acceptanceOpinion;
    }

    public AcceptorBean getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(AcceptorBean acceptor) {
        this.acceptor = acceptor;
    }

    public String getChargeInfo() {
        return chargeInfo;
    }

    public void setChargeInfo(String chargeInfo) {
        this.chargeInfo = chargeInfo;
    }

    public String getCodeRbacDepartment() {
        return codeRbacDepartment;
    }

    public void setCodeRbacDepartment(String codeRbacDepartment) {
        this.codeRbacDepartment = codeRbacDepartment;
    }

    public long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(long completeTime) {
        this.completeTime = completeTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRbacDepartment() {
        return idRbacDepartment;
    }

    public void setIdRbacDepartment(int idRbacDepartment) {
        this.idRbacDepartment = idRbacDepartment;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getNameAcAssetCategory() {
        return nameAcAssetCategory;
    }

    public void setNameAcAssetCategory(String nameAcAssetCategory) {
        this.nameAcAssetCategory = nameAcAssetCategory;
    }

    public String getNameAcAssetPosition() {
        return nameAcAssetPosition;
    }

    public void setNameAcAssetPosition(String nameAcAssetPosition) {
        this.nameAcAssetPosition = nameAcAssetPosition;
    }

    public String getNameAcAssetsProperty() {
        return nameAcAssetsProperty;
    }

    public void setNameAcAssetsProperty(String nameAcAssetsProperty) {
        this.nameAcAssetsProperty = nameAcAssetsProperty;
    }

    public String getNameAcEquipment() {
        return nameAcEquipment;
    }

    public void setNameAcEquipment(String nameAcEquipment) {
        this.nameAcEquipment = nameAcEquipment;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOrderSourceTitle() {
        return orderSourceTitle;
    }

    public void setOrderSourceTitle(String orderSourceTitle) {
        this.orderSourceTitle = orderSourceTitle;
    }

    public ReceiverBean getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverBean receiver) {
        this.receiver = receiver;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

    public String getSupervisoryCode() {
        return supervisoryCode;
    }

    public void setSupervisoryCode(String supervisoryCode) {
        this.supervisoryCode = supervisoryCode;
    }

    public int getSupervisoryStatus() {
        return supervisoryStatus;
    }

    public void setSupervisoryStatus(int supervisoryStatus) {
        this.supervisoryStatus = supervisoryStatus;
    }

    public String getSupervisoryStatusTitle() {
        return supervisoryStatusTitle;
    }

    public void setSupervisoryStatusTitle(String supervisoryStatusTitle) {
        this.supervisoryStatusTitle = supervisoryStatusTitle;
    }

    public List<ActionBean> getAction() {
        return action;
    }

    public void setAction(List<ActionBean> action) {
        this.action = action;
    }

    public List<FileBean> getFeedBackFileList() {
        return feedBackFileList;
    }

    public void setFeedBackFileList(List<FileBean> feedBackFileList) {
        this.feedBackFileList = feedBackFileList;
    }

    public List<FileBean> getSupervisoryFileList() {
        return supervisoryFileList;
    }

    public void setSupervisoryFileList(List<FileBean> supervisoryFileList) {
        this.supervisoryFileList = supervisoryFileList;
    }

    public static class AcceptorBean {
        /**
         * id : 153
         * id_ac_supervisory : 51
         * persontype : 1050
         * rbac_user_id : 153
         * rbac_user_name : 资产管理部经理
         */

        private int id;
        private int id_ac_supervisory;
        private int persontype;
        private int rbac_user_id;
        private String rbac_user_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId_ac_supervisory() {
            return id_ac_supervisory;
        }

        public void setId_ac_supervisory(int id_ac_supervisory) {
            this.id_ac_supervisory = id_ac_supervisory;
        }

        public int getPersontype() {
            return persontype;
        }

        public void setPersontype(int persontype) {
            this.persontype = persontype;
        }

        public int getRbac_user_id() {
            return rbac_user_id;
        }

        public void setRbac_user_id(int rbac_user_id) {
            this.rbac_user_id = rbac_user_id;
        }

        public String getRbac_user_name() {
            return rbac_user_name;
        }

        public void setRbac_user_name(String rbac_user_name) {
            this.rbac_user_name = rbac_user_name;
        }
    }

    public static class ReceiverBean {
        /**
         * id : 152
         * id_ac_supervisory : 51
         * persontype : 2040
         * rbac_user_id : 192
         * rbac_user_name : 工程管理专员
         */

        private int id;
        private int id_ac_supervisory;
        private int persontype;
        private int rbac_user_id;
        private String rbac_user_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId_ac_supervisory() {
            return id_ac_supervisory;
        }

        public void setId_ac_supervisory(int id_ac_supervisory) {
            this.id_ac_supervisory = id_ac_supervisory;
        }

        public int getPersontype() {
            return persontype;
        }

        public void setPersontype(int persontype) {
            this.persontype = persontype;
        }

        public int getRbac_user_id() {
            return rbac_user_id;
        }

        public void setRbac_user_id(int rbac_user_id) {
            this.rbac_user_id = rbac_user_id;
        }

        public String getRbac_user_name() {
            return rbac_user_name;
        }

        public void setRbac_user_name(String rbac_user_name) {
            this.rbac_user_name = rbac_user_name;
        }
    }

    public static class ActionBean {
        /**
         * creator : 资产管理部经理
         * files : []
         * gmtCreate : 2019-05-08 16:51:34
         * id : 212
         * notes : 完成
         * statusChange : 新建提交
         * supervisoryAction : 20
         * supervisoryId : 51
         */

        private String creator;
        private String gmtCreate;
        private int id;
        private String notes;
        private String statusChange;
        private int supervisoryAction;
        private int supervisoryId;
        private List<FileBean> files;

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
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

        public String getStatusChange() {
            return statusChange;
        }

        public void setStatusChange(String statusChange) {
            this.statusChange = statusChange;
        }

        public int getSupervisoryAction() {
            return supervisoryAction;
        }

        public void setSupervisoryAction(int supervisoryAction) {
            this.supervisoryAction = supervisoryAction;
        }

        public int getSupervisoryId() {
            return supervisoryId;
        }

        public void setSupervisoryId(int supervisoryId) {
            this.supervisoryId = supervisoryId;
        }

        public List<FileBean> getFiles() {
            return files;
        }

        public void setFiles(List<FileBean> files) {
            this.files = files;
        }
    }
    public class PrincipalBean{

        /**
         * id_ac_supervisory : 119
         * rbac_user_name : 陈仁宁-集团
         * id : 347
         * persontype : 10
         * rbac_user_id : 115
         */

        private int id_ac_supervisory;
        private String rbac_user_name;
        private int id;
        private int persontype;
        private int rbac_user_id;

        public int getId_ac_supervisory() {
            return id_ac_supervisory;
        }

        public void setId_ac_supervisory(int id_ac_supervisory) {
            this.id_ac_supervisory = id_ac_supervisory;
        }

        public String getRbac_user_name() {
            return rbac_user_name;
        }

        public void setRbac_user_name(String rbac_user_name) {
            this.rbac_user_name = rbac_user_name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPersontype() {
            return persontype;
        }

        public void setPersontype(int persontype) {
            this.persontype = persontype;
        }

        public int getRbac_user_id() {
            return rbac_user_id;
        }

        public void setRbac_user_id(int rbac_user_id) {
            this.rbac_user_id = rbac_user_id;
        }
    }
}