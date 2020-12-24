package com.jingkai.asset.function.manage.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.callback.OnItemClickRecyclerListener;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.manage.adapter.FacilityDynamicInfoAdapter;
import com.jingkai.asset.function.manage.entity.EquipmentDetailBean;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.utils.view.ViewUtil;
import com.jingkai.asset.widget.MeasureRecyclerView;
import com.jingkai.asset.widget.dialog.FacilityDynamicDetailDialog;

import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/12 18:21
 * Describe: 设备设施信息详情
 */
public class FacilityInformationActivity extends BaseActivity {
    //设备动态信息列表
    @Bind(R.id.mrv_standing_book_detail_info)
    MeasureRecyclerView mMrvInfo;
    //设备动态信息详情
    @Bind(R.id.ll_equipment_detail_dynamic_parent)
    LinearLayout mLlDynamicParent;
    //设备编号
    @Bind(R.id.tv_equipment_detail_number)
    TextView mTvNumber;
    //设备分类
    @Bind(R.id.tv_equipment_detail_classify)
    TextView mTvClassify;
    //设备名称
    @Bind(R.id.tv_equipment_detail_name)
    TextView mTvName;
    //位置
    @Bind(R.id.tv_equipment_detail_location)
    TextView mTvLocation;
    //设备状态
    @Bind(R.id.tv_equipment_detail_status)
    TextView mTvStatus;
    //品牌
    @Bind(R.id.tv_equipment_detail_brand)
    TextView mTvBrand;
    //首次启动日期
    @Bind(R.id.tv_equipment_detail_firtStartTime)
    TextView mTvFirtStartTime;
    //登记日期
    @Bind(R.id.tv_equipment_detail_registerTime)
    TextView mTvRegisterTime;
    //报修截止日期
    @Bind(R.id.tv_equipment_detail_repairExpirationDate)
    TextView mTvExpirationDate;
    //使用年限
    @Bind(R.id.tv_equipment_detail_durable_years)
    TextView mTvDurableYears;
    //强制报废年限
    @Bind(R.id.tv_equipment_detail_scrap_year)
    TextView mTvScrapYear;
    //供应商联系方式
    @Bind(R.id.tv_equipment_detail_supplier_phone)
    TextView mTvSupplierPhone;
    //采购价格
    @Bind(R.id.tv_equipment_detail_purchasePrice)
    TextView mTvPurchasePrice;
    private FacilityDynamicDetailDialog detailDialog;


    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_facility_information;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("设备设施信息");
    }

    @Override
    protected void loadData() {


        getDeviceDetail();

    }

    /**
     * 获取设备信息详情
     */
    private void getDeviceDetail() {
        int id = getIntent().getIntExtra("id", 0);
        params.clear();
        params.put("equipmentId", id);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.EQUIPMENT_DETAIL, params, new OKHttpManager.ResultCallback<BaseBean<EquipmentDetailBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseBean<EquipmentDetailBean> response) {
                hideRDialog();
                setPageData(response.getBody());
            }
        }, this);
    }

    /**
     * 渲染数据
     *
     * @param bean
     */
    private void setPageData(EquipmentDetailBean bean) {
        ViewUtil util = ViewUtil.getViewUtil();
        final EquipmentDetailBean.EquipmentBean equipmentBean = bean.getEquipment();
        util.setTextMessage(mTvNumber, "设备设施编号", equipmentBean.getEquipmentCode());
        util.setTextMessage(mTvClassify, "设备设施分类", equipmentBean.getAsssetCategoryName());
        util.setTextMessage(mTvName, "设备设施名称", equipmentBean.getEquipmentName());
        util.setTextMessage(mTvLocation, "位置", equipmentBean.getPosition());
        util.setTextMessage(mTvStatus, "设备设施状态", equipmentBean.getEquipmentStatusName());
        util.setTextMessage(mTvBrand, "品牌", equipmentBean.getBrand());
        util.setTextMessage(mTvFirtStartTime, "首次启动时间", equipmentBean.getGmtFirstStartDate());
        util.setTextMessage(mTvRegisterTime, "登记日期", equipmentBean.getGmtRecordDate());
        util.setTextMessage(mTvExpirationDate, "报修截止日期", equipmentBean.getRepairDate());
        util.setTextMessage(mTvDurableYears, "使用年限(年)", equipmentBean.getServiceLife() == 0 ? "" : String.valueOf(equipmentBean.getServiceLife()));
        util.setTextMessage(mTvScrapYear, "强制报废年限(年)", equipmentBean.getGmtScrappedYears());
        util.setTextMessage(mTvSupplierPhone, "供应商联系方式", equipmentBean.getSupplierPhone());
        util.setTextMessage(mTvPurchasePrice, "采购价格(元)", equipmentBean.getPurchasePrice() == 0 ? "" : String.valueOf(equipmentBean.getPurchasePrice()));

        //加载设备动态信息列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMrvInfo.setLayoutManager(layoutManager);
        final List<EquipmentDetailBean.RepairListBean> list = bean.getRepairList();
        if (list != null && list.size() > 0) {
            //mLlDynamicParent.setVisibility(View.VISIBLE);
            FacilityDynamicInfoAdapter adapter = new FacilityDynamicInfoAdapter(list, this);
            mMrvInfo.setAdapter(adapter);
            adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (detailDialog == null)
                        detailDialog = new FacilityDynamicDetailDialog(FacilityInformationActivity.this);
                    detailDialog.setData(equipmentBean, list.get(position));
                    detailDialog.show();
                }
            });
        }


    }


}
