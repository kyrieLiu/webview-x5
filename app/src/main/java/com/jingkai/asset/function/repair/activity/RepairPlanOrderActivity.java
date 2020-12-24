package com.jingkai.asset.function.repair.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.callback.OnItemClickRecyclerListener;
import com.jingkai.asset.callback.OnRecyclerLoadingListener;
import com.jingkai.asset.common.entity.RequestBean;
import com.jingkai.asset.config.RxConstant;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.repair.adapter.RepairPlanOrderAdapter;
import com.jingkai.asset.function.operation.entity.RepairPlanOrderBean;
import com.jingkai.asset.manager.RxManager;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.widget.xrecycler.XRecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by liuyin on 2019/4/16 9:30
 * Description:计划修缮单页面
 */
public class RepairPlanOrderActivity extends BaseActivity {
    @Bind(R.id.ll_right)
    LinearLayout mLlRight;
    @Bind(R.id.rv_common_list)
    XRecyclerView mRvList;
    private RepairPlanOrderAdapter<RepairPlanOrderBean.ItemsBean> adapter;

    private RxManager rxManager;

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
        setTitle("修缮计划单");
        rightVisible(R.mipmap.ic_search);

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
    }

    @Override
    protected void loadData() {

        adapter = new RepairPlanOrderAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(layoutManager);
        mRvList.setAdapter(adapter);

        adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
            @Override
            public void onItemClick(View view, int position) {
                RepairPlanOrderBean.ItemsBean bean = adapter.getList().get(position);

                RepairProjectDetailActivity.start(RepairPlanOrderActivity.this, bean.getId(), 1, 0,bean.getRepairStatusTitle());
            }
        });

        mRvList.refresh();

        initObserver();
    }

    /**
     * 观察修缮验收
     */
    private void initObserver() {
        rxManager = new RxManager();
        rxManager.on(RxConstant.RX_REPAIR_CHECK, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                requestData(1);
            }
        });
    }

    /**
     * 请求资产修缮列表数据
     *
     * @param page
     */
    private void requestData(final int page) {
        final RequestBean request = new RequestBean();
        RequestBean.PageableBean pageableBean = new RequestBean.PageableBean();
        pageableBean.setCurrent(page);
        request.setPageable(pageableBean);
        RequestBean.EntityBean entityBean = new RequestBean.EntityBean();
        entityBean.setCustomQuery("");
        request.setEntity(entityBean);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.ASSETS_REPAIR_LIST, request, new OKHttpManager.ResultCallback<BaseBean<RepairPlanOrderBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
                adapter.setErrorReqList(message, mRvList);
            }

            @Override
            public void onResponse(BaseBean<RepairPlanOrderBean> response) {
                hideRDialog();
                final List<RepairPlanOrderBean.ItemsBean> list = response.getBody().getItems();
                adapter.setSuccessReqList(list, request.getPageable().getSize(), page, mRvList,"暂无修缮计划单");
            }
        }, this);
    }

    @OnClick({R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                startActivity(new Intent(RepairPlanOrderActivity.this, RepairPlanSearchActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxManager.clear();
        rxManager = null;
    }
}
