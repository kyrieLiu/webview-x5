package com.jingkai.asset.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.function.manage.entity.EquipmentDetailBean;
import com.jingkai.asset.utils.view.ViewUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuyin on 2019/4/12 11:02
 * Description:设备动态详情
 */
public class FacilityDynamicDetailDialog extends Dialog {
    //设备编号
    @Bind(R.id.tv_dialog_facility_number)
    TextView mTvNumber;
    //设备分类
    @Bind(R.id.tv_dialog_facility_classify)
    TextView mTvClassify;
    //设备名称
    @Bind(R.id.tv_dialog_facility_name)
    TextView mTvName;
    //品牌
    @Bind(R.id.tv_dialog_facility_brand)
    TextView mTvBrand;
    //位置
    @Bind(R.id.tv_dialog_facility_location)
    TextView mTvLocation;
    //记录时间
    @Bind(R.id.tv_dialog_facility_time)
    TextView mTvTime;
    //执行人
    @Bind(R.id.tv_dialog_facility_executor)
    TextView mTvExecutor;
    //动态类别
    @Bind(R.id.tv_dialog_facility_dynamic_type)
    TextView mTvType;
    //详情
    @Bind(R.id.tv_dialog_facility_detail)
    TextView mTvDetail;

    public FacilityDynamicDetailDialog(@NonNull Context context) {
        super(context, R.style.ThemeCustomDialog);
        setContentView(R.layout.dialog_facility_info);

        ButterKnife.bind(this);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
    }


    @OnClick(R.id.iv_dialog_facility_close)
    public void onViewClicked() {
        dismiss();
    }

    /**
     * 加载数据
     *
     * @param equipmentBean
     * @param repairListBean
     */
    public void setData(EquipmentDetailBean.EquipmentBean equipmentBean, EquipmentDetailBean.RepairListBean repairListBean) {
        setTextMessage(mTvNumber, "设备设施编号", equipmentBean.getEquipmentCode());
        setTextMessage(mTvClassify, "设备设施分类", equipmentBean.getAsssetCategoryName());
        setTextMessage(mTvName, "设备设施名称", equipmentBean.getEquipmentName());
        setTextMessage(mTvLocation, "位置", equipmentBean.getPosition());
        setTextMessage(mTvBrand, "品牌", equipmentBean.getBrand());
        setTextMessage(mTvTime, "记录时间", repairListBean.getGmtCreateDateStr());
        setTextMessage(mTvExecutor, "责任人", repairListBean.getCreator());
        setTextMessage(mTvType, "动态类别", repairListBean.getRepairTypeName());
        setTextMessage(mTvDetail, "详情", repairListBean.getNotes());

    }

    /**
     * 将数据填充到对应控件上
     *
     * @param textView
     * @param title
     * @param message
     */
    private void setTextMessage(TextView textView, String title, String message) {
        ViewUtil viewUtil = ViewUtil.getViewUtil();
        viewUtil.setTextMessage(textView, title, message);
    }
}
