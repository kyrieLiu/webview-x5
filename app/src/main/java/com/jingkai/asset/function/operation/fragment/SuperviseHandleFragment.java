package com.jingkai.asset.function.operation.fragment;

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
import com.jingkai.asset.function.operation.entity.RepairFinalReqBean;
import com.jingkai.asset.function.operation.entity.SuperviseHandleAcceptanceBean;
import com.jingkai.asset.function.supervise.activity.SuperviseHandleDetailActivity;
import com.jingkai.asset.function.supervise.adapter.SuperviseHandleAcceptanceAdapter;
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
 * Description:督查督办验收碎片,已验收和待验收复用页面
 */
public class SuperviseHandleFragment extends BaseFragment {
    @Bind(R.id.xrv_common_list)
    XRecyclerView mXrvList;

    private int pageIndex;//当前页数  1已验收 2待验收
    private SuperviseHandleAcceptanceAdapter<SuperviseHandleAcceptanceBean.ItemsBean> adapter;
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
        adapter = new SuperviseHandleAcceptanceAdapter(getActivity(), pageIndex);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mXrvList.setLayoutManager(layoutManager);
        mXrvList.setAdapter(adapter);

        adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
            @Override
            public void onItemClick(View view, int position) {
                SuperviseHandleAcceptanceBean.ItemsBean itemsBean = adapter.getList().get(position);
//                if (pageIndex == 1) {//已验收
//                    SuperviseHandleDetailActivity.start(getActivity(),itemsBean.getId(),0);
//                } else {
                SuperviseHandleDetailActivity.start(getActivity(), itemsBean.getId(), Integer.parseInt(itemsBean.getIsRead()));
                //}
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
        //监听工单状态有没又更新为已读
        rxManager.on(RxConstant.RX_SUPERVISE_HANDLE_UPDATE, new Action1<Integer>() {
            @Override
            public void call(Integer id) {
                //将传递过来的id对应的列表实体进行更新为已读状态
                List<SuperviseHandleAcceptanceBean.ItemsBean> list = adapter.getList();
                for (SuperviseHandleAcceptanceBean.ItemsBean itemsBean : list) {
                    if (itemsBean.getId() == id) {
                        itemsBean.setIsRead("1");
                    }
                }
                adapter.setList(list);
            }
        });
        //观察督查督办验收
        rxManager.on(RxConstant.RX_SUPERVISE_ACCEPT, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                requestData(1);
            }
        });
    }

    /**
     * @param page
     */
    private void requestData(final int page) {
        final RequestBean request = initRequestParams();
        request.getPageable().setCurrent(page);
        RepairFinalReqBean bean = new RepairFinalReqBean();
        bean.setField("supervisoryStatus");
        bean.setOp("eq");
        if (pageIndex == 1) {//已验收
            bean.setData("40");
        } else {//待验收
            bean.setData("30");
        }
        List<RepairFinalReqBean> list = new ArrayList<>();
        list.add(bean);
        RequestBean.CondBean condBean = request.getCond();
        condBean.setRules(list);
        condBean.setGroupOp("AND");
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.SUPERVISE_LIST, request, new OKHttpManager.ResultCallback<BaseBean<SuperviseHandleAcceptanceBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
                adapter.setErrorReqList(message, mXrvList);
            }

            @Override
            public void onResponse(BaseBean<SuperviseHandleAcceptanceBean> response) {
                hideRDialog();
                SuperviseHandleAcceptanceBean acceptanceBean = response.getBody();
                List<SuperviseHandleAcceptanceBean.ItemsBean> list = null;
                if (acceptanceBean != null) list = acceptanceBean.getItems();
                adapter.setSuccessReqList(list, request.getPageable().getSize(), page, mXrvList,"暂无督查督办验收任务");

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
