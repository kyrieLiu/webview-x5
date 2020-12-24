package com.jingkai.asset.function.repair.entity;

import com.jingkai.asset.function.supervise.entity.SuperviseDetailBean;

import java.util.List;

/**
 * Created by liuyin on 2019/5/17 16:13
 * Description:审核过程需要页面传递,数据过大不能使用Intent
 */
public class ProcessCacheBean {
    /**
     * 修缮详情审核过程数据
     */
    public static List<RepairDetailBean.ProcessBean> processList;
    /**
     * 督查督办验收过程数据
     */
    public static List<SuperviseDetailBean.ActionBean> actionList;
}
