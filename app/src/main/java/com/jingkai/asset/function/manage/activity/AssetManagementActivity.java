package com.jingkai.asset.function.manage.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.common.adapter.CommonFragmentPagerAdapter;
import com.jingkai.asset.common.helper.CodePermissionHelper;
import com.jingkai.asset.common.helper.PermissionCode;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.manage.entity.GardenInfoBean;
import com.jingkai.asset.function.manage.fragment.AssetStandingBookFragment;
import com.jingkai.asset.function.manage.fragment.FacilityStandingBookFragment;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.widget.dialog.SelectGardenDialog;
import com.jingkai.asset.widget.mark_tablayout.MarkTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liuyin on 2019/4/11 15:54
 * Describe: 资产管理页面
 */
public class AssetManagementActivity extends BaseActivity {

    @Bind(R.id.rl_assets_management_park)
    RelativeLayout mRlPark;
    @Bind(R.id.mtl_assets_management)
    MarkTabLayout mMtlManagement;
    @Bind(R.id.vp_assets_management)
    ViewPager mViewPager;
    //搜索
    @Bind(R.id.iv_asset_management_search)
    ImageView mIvSearch;
    //园区名称
    @Bind(R.id.tv_assets_manager_gardenName)
    TextView mTvGardenName;

    private SelectGardenDialog gardenDialog;
    private AssetStandingBookFragment assetFragment;
    private FacilityStandingBookFragment deviceFragment;

    private List<GardenInfoBean.ItemsBean> gardenList;


    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_assets_management;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void loadData() {

        getParkData();
    }

    @OnClick({R.id.rl_assets_management_park})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_assets_management_park:
                selectPark();
                break;
        }
    }

    public void selectPark() {
        if (gardenDialog == null)
            gardenDialog = new SelectGardenDialog(AssetManagementActivity.this);
        gardenDialog.setData(gardenList);
        gardenDialog.setListener(new SelectGardenDialog.GardenSelectListener() {
            @Override
            public void selectCallback(GardenInfoBean.ItemsBean bean) {
                gardenDialog.dismiss();
                if (bean != null) {
                    mTvGardenName.setText(bean.getName());
                    if (assetFragment != null) assetFragment.updateParkData(bean.getId());
                    if (deviceFragment != null) deviceFragment.updateParkData(bean.getId());
                }
            }
        });
        gardenDialog.show();
    }

    /**
     * 获取园区列表
     */
    private void getParkData() {
        params.clear();
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.PARK_LIST, params, new OKHttpManager.ResultCallback<BaseBean<GardenInfoBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseBean<GardenInfoBean> response) {
                hideRDialog();

                gardenList = response.getBody().getItems();
                GardenInfoBean.ItemsBean itemsBean = gardenList.get(0);
                itemsBean.setIsChecked(1);

                mTvGardenName.setText(itemsBean.getName());

                setPageData(itemsBean.getId());

            }
        }, this);
    }

    /**
     * 根据当前园区加载页面页面数据
     * @param id
     */
    private void setPageData(int id) {
        List<String> titleList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();
        //判断资产台账权限,添加资产台账页面
        if (CodePermissionHelper.havePermission(PermissionCode.MANAGE_HOUSE_STANDING_BOOK.getCode())) {
            titleList.add("资产台账");
            assetFragment = AssetStandingBookFragment.getInstance(id);
            fragmentList.add(assetFragment);
        }
        //判断设备设施台账权限,添加设备设施台账页面
        if (CodePermissionHelper.havePermission(PermissionCode.MANAGE_HOUSE_EQUIPMENT_BOOK.getCode())) {
            titleList.add("设备设施台账");
            deviceFragment = FacilityStandingBookFragment.getInstance(id);
            fragmentList.add(deviceFragment);
        }

        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(this, getSupportFragmentManager(), fragmentList, titleList);
        mViewPager.setAdapter(adapter);
        mMtlManagement.setupWithViewPager(mViewPager);
    }

}
