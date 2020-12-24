package com.jingkai.asset.function.main.entity;

/**
 * Created by liuyin on 2019/5/27 13:44
 * Description:
 */
public class HeadImageUploadBean {
    /**
     * groupName : group1
     * filePath : M00/00/4B/rBMBOFzreaeAfG95AACtKtQO6eM234.jpg
     * fileUrl : http://pocketbook.document.jingcaiwang.cn/group1/M00/00/4B/rBMBOFzreaeAfG95AACtKtQO6eM234.jpg
     * thumbUrl : null
     * fileName : mmexport1558519669465.jpg
     */

    private String groupName;
    private String filePath;
    private String fileUrl;
    private Object thumbUrl;
    private String fileName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Object getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(Object thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
