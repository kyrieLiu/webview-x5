package com.jingkai.asset.function.manage.entity;

import java.util.List;

/**
 * Created by liuyin on 2019/4/17 17:30
 * Description:房间树数据
 */
public class HouseTreeBean {
    /**
     * id : 1
     * text : 亦城财富中心
     * checked : null
     * children : [{"id":"247","text":"1号楼","checked":null,"children":[],"isParent":false,"chkDisabled":null,"open":false,"attr":{"level":3,"assetPropertyCount":1,"isAssetProperty":false}},{"id":"40","text":"阿赖的房子","checked":null,"children":null,"isParent":false,"chkDisabled":null,"open":null,"attr":{"level":2,"statusName":"自用","isAssetProperty":true}}]
     * isParent : true
     * chkDisabled : null
     * open : true
     * attr : {"level":1,"assetPropertyCount":6,"isAssetProperty":false}
     */

    private String id;
    private String text;
    private Object checked;
    private boolean isParent;
    private Object chkDisabled;
    private boolean open;
    private AttrBean attr;
    private String showID;
    private List<HouseTreeBean> children;


    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public String getShowID() {
        return showID;
    }

    public void setShowID(String showID) {
        this.showID = showID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getChecked() {
        return checked;
    }

    public void setChecked(Object checked) {
        this.checked = checked;
    }

    public boolean isIsParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public Object getChkDisabled() {
        return chkDisabled;
    }

    public void setChkDisabled(Object chkDisabled) {
        this.chkDisabled = chkDisabled;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public AttrBean getAttr() {
        return attr;
    }

    public void setAttr(AttrBean attr) {
        this.attr = attr;
    }

    public List<HouseTreeBean> getChildren() {
        return children;
    }

    public void setChildren(List<HouseTreeBean> children) {
        this.children = children;
    }

    public static class AttrBean {
        /**
         * level : 1
         * assetPropertyCount : 6
         * isAssetProperty : false
         */

        private int level;
        private int assetPropertyCount;
        private boolean isAssetProperty;
        private String statusName;

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getAssetPropertyCount() {
            return assetPropertyCount;
        }

        public void setAssetPropertyCount(int assetPropertyCount) {
            this.assetPropertyCount = assetPropertyCount;
        }

        public boolean isIsAssetProperty() {
            return isAssetProperty;
        }

        public void setIsAssetProperty(boolean isAssetProperty) {
            this.isAssetProperty = isAssetProperty;
        }
    }


}
