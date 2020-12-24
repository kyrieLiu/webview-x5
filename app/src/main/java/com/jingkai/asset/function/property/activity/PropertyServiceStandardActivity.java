package com.jingkai.asset.function.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.callback.OnItemClickRecyclerListener;
import com.jingkai.asset.callback.OnRecyclerLoadingListener;
import com.jingkai.asset.common.entity.RequestBean;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.manage.entity.GardenInfoBean;
import com.jingkai.asset.function.operation.adapter.PropertyServiceStandardAdapter;
import com.jingkai.asset.function.operation.entity.PropertyServiceStandardBean;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.widget.dialog.SelectGardenDialog;
import com.jingkai.asset.widget.xrecycler.XRecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liuyin on 2019/4/17 9:50
 * Description:物业服务标准管理列表页面
 */
public class PropertyServiceStandardActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.rv_property_list)
    XRecyclerView mRvList;
    @Bind(R.id.tv_propperty_search_content)
    TextView mTvPark;

    private SelectGardenDialog gardenDialog;

    private PropertyServiceStandardAdapter<PropertyServiceStandardBean.ItemsBean> adapter;
    /**
     * 园区ID
     */
    private String parkId;

    private List<GardenInfoBean.ItemsBean> list;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_property_service_list;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("物业服务标准管理");
        rightVisible(R.mipmap.ic_search);
    }

    @Override
    protected void loadData() {
        adapter = new PropertyServiceStandardAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(layoutManager);
        mRvList.setAdapter(adapter);
        adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
            @Override
            public void onItemClick(View view, int position) {
                PropertyServiceStandardBean.ItemsBean itemsBean = adapter.getList().get(position);
                PropertyWebActivity.start(PropertyServiceStandardActivity.this, itemsBean.getDocUrl(), "物业服务管理标准",itemsBean.getDocId()+"");
            }
        });
        mRvList.setLoadingListener(new OnRecyclerLoadingListener() {
            @Override
            public void onRefresh() {
                requestData(1);
            }

            @Override
            public void onLoadMore(final int reqPage) {
                requestData(reqPage);
            }
        });
        mRvList.refresh();
        requestParkData(false);
    }

    private void requestData(final int page) {
        final RequestBean request = initRequestParams();
        request.getPageable().setCurrent(page);
        RequestBean.EntityBean entityBean = new RequestBean.EntityBean();
        entityBean.setTitle("");
        if (!TextUtils.isEmpty(parkId) && !("0".equals(parkId))) {
            entityBean.setAcParkId(parkId);
        }
        request.setEntity(entityBean);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.GET_DOC_LIST, request, new OKHttpManager.ResultCallback<BaseBean<PropertyServiceStandardBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
                adapter.setErrorReqList(message, mRvList);
            }

            @Override
            public void onResponse(BaseBean<PropertyServiceStandardBean> response) {
                hideRDialog();
                final List<PropertyServiceStandardBean.ItemsBean> list = response.getBody().getItems();
                adapter.setSuccessReqList(list, request.getPageable().getSize(), page, mRvList, "暂无物业服务标准管理");
            }
        }, this);
    }

    /**
     * 跳转到搜索页
     *
     * @param view
     */
    @OnClick({R.id.iv_property_search, R.id.ll_property_select_park})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_property_search:
                Intent intent = new Intent(this, PropertyServiceStandardSearchActivity.class);
                intent.putExtra("parkId", parkId);
                startActivity(intent);
                break;
            case R.id.ll_property_select_park:
                getParkData();
                break;
        }
    }

    private void getParkData() {
        if (gardenDialog == null) {
            requestParkData(true);
        } else {
            gardenDialog.show();
        }
    }

    /**
     * 获取园区列表
     */
    private void requestParkData(final boolean showDialog) {
        params.clear();
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.PROPERTY_PARK_LIST, params, new OKHttpManager.ResultCallback<BaseBean<GardenInfoBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseBean<GardenInfoBean> response) {
                hideRDialog();
                list = response.getBody().getItems();
                GardenInfoBean.ItemsBean itemsBean = new GardenInfoBean.ItemsBean();
                itemsBean.setName("全部");
                itemsBean.setIsChecked(1);
                list.add(0, itemsBean);

                gardenDialog = new SelectGardenDialog(PropertyServiceStandardActivity.this);
                gardenDialog.setData(list);
                gardenDialog.setListener(new SelectGardenDialog.GardenSelectListener() {
                    @Override
                    public void selectCallback(GardenInfoBean.ItemsBean bean) {
                        gardenDialog.dismiss();
                        if (bean != null) {
                            mTvPark.setText(bean.getName());
                            parkId = String.valueOf(bean.getId());
                            mRvList.setReqPage(1);
                            requestData(1);
                        }
                    }
                });

                if (showDialog) gardenDialog.show();

            }
        }, this);
    }


}
