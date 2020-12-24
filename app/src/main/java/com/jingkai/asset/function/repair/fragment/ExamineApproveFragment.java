package com.jingkai.asset.function.repair.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.base.BaseFragment;
import com.jingkai.asset.callback.OnItemClickRecyclerListener;
import com.jingkai.asset.callback.OnRecyclerLoadingListener;
import com.jingkai.asset.common.entity.RequestBean;
import com.jingkai.asset.config.RxConstant;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.operation.adapter.ExamineApprovalAdapter;
import com.jingkai.asset.function.operation.entity.FinalExamineBean;
import com.jingkai.asset.function.operation.entity.RepairFinalReqBean;
import com.jingkai.asset.function.repair.activity.RepairProjectDetailActivity;
import com.jingkai.asset.manager.RxManager;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.widget.xrecycler.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.functions.Action1;

/**
 * Created by liuyin on 2019/4/16 11:35
 * Description:修缮终审碎片,已审批和待审批复用页面
 */
public class ExamineApproveFragment extends BaseFragment {
    @Bind(R.id.xrv_common_list)
    XRecyclerView mXrvList;

    private int pageIndex;//当前页数  2已审批 1待审批
    private ExamineApprovalAdapter<FinalExamineBean.ItemsBean> adapter;

    private RxManager rxManager;

    @Override
    protected int getContentId() {
        return R.layout.common_fragment_xrecycler_list;
    }

    @Override
    protected void initView(Bundle bundle) {
        pageIndex = bundle.getInt("pageIndex");
    }

    @Override
    protected void loadData() {
        adapter = new ExamineApprovalAdapter(getActivity(), pageIndex);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mXrvList.setLayoutManager(layoutManager);
        mXrvList.setAdapter(adapter);

        adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
            @Override
            public void onItemClick(View view, int position) {
                FinalExamineBean.ItemsBean bean = adapter.getList().get(position);
                Intent intent = new Intent(getActivity(), RepairProjectDetailActivity.class);
                intent.putExtra("id", bean.getId());
                intent.putExtra("isRead", bean.getIsRead());
                if (pageIndex == 2) {
                    intent.putExtra("intentType", 2);
                    intent.putExtra("status", 0);
                } else {
                    intent.putExtra("intentType", 2);
                    intent.putExtra("status", 1);
                }
                startActivity(intent);
            }
        });

        mXrvList.setLoadingListener(new OnRecyclerLoadingListener() {
            @Override
            public void onRefresh() {
                requestData(1);
            }

            @Override
            public void onLoadMore(int reqPage) {
                requestData(reqPage);
            }
        });
        mXrvList.refresh();
        initListener();
    }

    /**
     * 监听红点更新消息
     */
    private void initListener() {
        rxManager = new RxManager();
        //监听工单状态有没有更新为已读
        rxManager.on(RxConstant.RX_REPAIR_ORDER_UPDATE, new Action1<Integer>() {
            @Override
            public void call(Integer id) {
                //将传递过来的id对应的列表实体进行更新为已读状态
                List<FinalExamineBean.ItemsBean> list = adapter.getList();
                for (FinalExamineBean.ItemsBean itemsBean : list) {
                    if (itemsBean.getId() == id) {
                        itemsBean.setIsRead(1);
                    }
                }
                adapter.setList(list);
            }
        });
        //观察验收
        rxManager.on(RxConstant.RX_REPAIR_CHECK, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                requestData(1);
            }
        });
    }

    /**
     * 请求修缮终审列表数据
     *
     * @param page
     */
    private void requestData(final int page) {
        final RequestBean request = initRequestParams();
        request.getPageable().setCurrent(page);
        RepairFinalReqBean bean = new RepairFinalReqBean();
        bean.setField("status");
        bean.setOp("eq");
        if (pageIndex == 2) {//已审批
            bean.setData("1");
        } else {//待审批
            bean.setData("0");
        }
        List<RepairFinalReqBean> list = new ArrayList<>();
        list.add(bean);
        RequestBean.CondBean condBean = request.getCond();
        condBean.setRules(list);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.REPAIR_FINAL_LIST, request, new OKHttpManager.ResultCallback<BaseBean<FinalExamineBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
                adapter.setErrorReqList(message, mXrvList);
            }

            @Override
            public void onResponse(BaseBean<FinalExamineBean> response) {
                hideRDialog();
                final List<FinalExamineBean.ItemsBean> list = response.getBody().getItems();
                adapter.setSuccessReqList(list, request.getPageable().getSize(), page, mXrvList, "暂无修缮终审任务");
            }
        }, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rxManager != null) {
            rxManager.clear();
            rxManager = null;
        }
    }
}
