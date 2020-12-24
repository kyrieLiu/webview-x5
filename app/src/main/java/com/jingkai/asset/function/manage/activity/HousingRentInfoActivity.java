package com.jingkai.asset.function.manage.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.base.BaseListBean;
import com.jingkai.asset.callback.OnRecyclerLoadingListener;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.manage.adapter.HouseRentAdapter;
import com.jingkai.asset.function.manage.entity.HouseRentContractBean;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.utils.view.ViewUtil;
import com.jingkai.asset.widget.xrecycler.ItemLinearDivider;
import com.jingkai.asset.widget.xrecycler.XRecyclerView;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/15 11:18
 * Description:房屋出租信息列表
 */
public class HousingRentInfoActivity extends BaseActivity {
    @Bind(R.id.rv_common_list)
    XRecyclerView mRvList;
    private HouseRentAdapter adapter;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.common_activity_list;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("出租详情");
    }

    @Override
    protected void loadData() {

        adapter = new HouseRentAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(manager);
        mRvList.setAdapter(adapter);
        mRvList.addItemDecoration(new ItemLinearDivider(ViewUtil.dp2px(10), 0, 0, getResources().getColor(R.color.gray_light_color)));


        mRvList.setLoadingListener(new OnRecyclerLoadingListener() {
            @Override
            public void onRefresh() {
                getContractData();
            }

            @Override
            public void onLoadMore(int reqPage) {

            }
        });

        getContractData();

    }

    /**
     * 获取合同数据
     */
    private void getContractData() {

        int id = getIntent().getIntExtra("id", 0);

        params.clear();
        params.put("assetsId", id);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.ASSETS_CONTRACT_LIST, params, new OKHttpManager.ResultCallback<BaseListBean<HouseRentContractBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseListBean<HouseRentContractBean> response) {
                hideRDialog();
                adapter.setSuccessReqList(response.getBody(), -1, 1, mRvList, "暂无租售详情信息");
            }
        }, this);
    }

}
