package com.jingkai.asset.function.repair.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.callback.OnItemClickRecyclerListener;
import com.jingkai.asset.callback.OnRecyclerLoadingListener;
import com.jingkai.asset.common.entity.RequestBean;
import com.jingkai.asset.config.RxConstant;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.operation.entity.FinalExamineBean;
import com.jingkai.asset.function.operation.entity.RepairApprovalBean;
import com.jingkai.asset.function.operation.entity.RepairPlanOrderBean;
import com.jingkai.asset.function.repair.adapter.RepairApprovalAdapter;
import com.jingkai.asset.manager.RxManager;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.LogUtil;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.widget.xrecycler.XRecyclerView;

import java.util.List;

import butterknife.Bind;
import rx.functions.Action1;

/**
 * Created by zhangyang
 * Date: 2019/8/28
 * Description: 修缮审批
 */
public class RepairApprovalActivity extends BaseActivity {


    @Bind(R.id.rv_repair_approval_list)
    XRecyclerView mRvRepairApprovalList;

    RepairApprovalAdapter<RepairApprovalBean.ItemsBean> adapter;
    RxManager rxManager;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_repair_approval;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("修缮审批");

        mRvRepairApprovalList.setLoadingListener(new OnRecyclerLoadingListener() {
            @Override
            public void onRefresh() {
                requestData(1);
            }

            @Override
            public void onLoadMore(int reqPage) {
                requestData(reqPage);
            }
        });
    }

    @Override
    protected void loadData() {
        adapter = new RepairApprovalAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvRepairApprovalList.setLayoutManager(layoutManager);
        mRvRepairApprovalList.setAdapter(adapter);

        adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
            @Override
            public void onItemClick(View view, int position) {
                RepairApprovalBean.ItemsBean bean = adapter.getList().get(position);

                RepairProjectDetailActivity.start(RepairApprovalActivity.this, bean.getId(), 3, bean.getIsRead(),bean.getRepairPlanStatus());
            }
        });

        mRvRepairApprovalList.refresh();

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

        //监听工单状态有没有更新为已读
        rxManager.on(RxConstant.RX_REPAIR_ORDER_UPDATE, new Action1<Integer>() {
            @Override
            public void call(Integer id) {
                //将传递过来的id对应的列表实体进行更新为已读状态
                LogUtil.i("id======="+id);
                List<RepairApprovalBean.ItemsBean> list = adapter.getList();
                for (RepairApprovalBean.ItemsBean itemsBean : list) {
                    if (itemsBean.getId() == id) {
                        itemsBean.setIsRead(1);
                    }
                }
                adapter.setList(list);
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
        RequestBean.CondBean condBean = new RequestBean.CondBean();
        request.setCond(condBean);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.REPAIR_APPROVAL_LIST, request, new OKHttpManager.ResultCallback<BaseBean<RepairApprovalBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
                adapter.setErrorReqList(message, mRvRepairApprovalList);
            }

            @Override
            public void onResponse(BaseBean<RepairApprovalBean> response) {
                hideRDialog();
                final List<RepairApprovalBean.ItemsBean> list = response.getBody().getItems();
                adapter.setSuccessReqList(list, request.getPageable().getSize(), page, mRvRepairApprovalList, "暂无修缮审批单");
            }
        }, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rxManager != null){
            rxManager.clear();
            rxManager = null;
        }
    }
}
