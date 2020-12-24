package com.jingkai.asset.function.manage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.manage.entity.HouseDetailBean;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.utils.view.ViewUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liuyin on 2019/4/15 11:10
 * Describe: 房产详情
 */
public class HousingDetailActivity extends BaseActivity {
    //资产编号
    @Bind(R.id.tv_housing_detail_number)
    TextView mTvNumber;
    //出租状态
    @Bind(R.id.tv_housing_detail_status)
    TextView mTvStatus;
    //资产分类
    @Bind(R.id.tv_housing_detail_classify)
    TextView mTvClassify;
    //房产名称
    @Bind(R.id.tv_housing_detail_name)
    TextView mTvName;
    //房产位置
    @Bind(R.id.tv_housing_detail_location)
    TextView mTvLocation;
    //建筑面积
    @Bind(R.id.tv_housing_detail_area)
    TextView mTvArea;
    //用途规划
    @Bind(R.id.tv_housing_detail_use_plan)
    TextView mTvUsePlan;
    //租金底价
    @Bind(R.id.tv_housing_detail_upset_rent)
    TextView mTvUpsetRent;
    //创建人
    @Bind(R.id.tv_housing_detail_founder)
    TextView mTvFounder;
    //创建时间
    @Bind(R.id.tv_housing_detail_create_time)
    TextView mTvCreateTime;
    //最后修改人
    @Bind(R.id.tv_housing_detail_last_modifier)
    TextView mTvLastModifier;
    //最后修改时间
    @Bind(R.id.tv_housing_detail_last_modify_time)
    TextView mTvLastModifyTime;
    //查看出租详情
    @Bind(R.id.bt_housing_detail_see)
    Button mBtSeeDetail;
    @Bind(R.id.fl_house_detail_parent)
    FrameLayout mFlDetailParent;

    private int houseId;
    private String status;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_housing_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("房产详情");
        status = getIntent().getStringExtra("status");
    }

    @Override
    protected void loadData() {
        int id = getIntent().getIntExtra("id", 0);
        params.clear();
        params.put("assetsId", id);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.ASSETS_DETAIL, params, new OKHttpManager.ResultCallback<BaseBean<HouseDetailBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseBean<HouseDetailBean> response) {
                hideRDialog();
                HouseDetailBean.AssetsBean bean = response.getBody().getAssets();

                houseId = bean.getId();

                ViewUtil util = ViewUtil.getViewUtil();
                util.setTextMessage(mTvNumber, "房产编号", bean.getAssetsPropertyCode());
                util.setTextMessage(mTvClassify, "房产分类", bean.getAssetCategoryName());
                util.setTextMessage(mTvName, "房产名称", bean.getAssetsName());
                util.setTextMessage(mTvLocation, "房产位置", bean.getPositionName());
                util.setTextMessage(mTvArea, "建筑面积", bean.getAcreage());
                util.setTextMessage(mTvUsePlan, "用途规划", bean.getUseTypeTitle());
                util.setTextMessage(mTvUpsetRent, "租金底价", bean.getPaymentStatus());
                util.setTextMessage(mTvFounder, "创建人", bean.getCreator());
                util.setTextMessage(mTvCreateTime, "创建时间", bean.getGmtCreate());
                util.setTextMessage(mTvLastModifier, "最后修改人", bean.getEditor());
                util.setTextMessage(mTvLastModifyTime, "最后修改时间", bean.getGmtModified());
                mTvStatus.setText(bean.getStatusName());

                //已出租的可以查看租售详情
                if ("已出租".equals(status)) {
                    mFlDetailParent.setVisibility(View.VISIBLE);
                }
            }
        }, this);
    }

    @OnClick(R.id.bt_housing_detail_see)
    public void onViewClicked() {
        Intent intent = new Intent(this, HousingRentInfoActivity.class);
        intent.putExtra("id", houseId);
        startActivity(intent);
    }
}
