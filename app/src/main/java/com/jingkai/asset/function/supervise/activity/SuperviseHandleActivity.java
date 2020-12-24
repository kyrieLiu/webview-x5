package com.jingkai.asset.function.supervise.activity;

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
import com.jingkai.asset.function.supervise.adapter.SuperviseHandleAdapter;
import com.jingkai.asset.function.supervise.entity.SuperviseHandleBean;
import com.jingkai.asset.manager.RxManager;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.widget.xrecycler.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.functions.Action1;

/**
 * Created by liuyin on 2019/4/16 15:11
 * Description: 督查督办页面
 */
public class SuperviseHandleActivity extends BaseActivity {
    @Bind(R.id.rv_common_list)
    XRecyclerView mRvList;
    private SuperviseHandleAdapter<SuperviseHandleBean.ItemsBean> adapter;

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
        setTitle("督查督办");
    }

    @Override
    protected void loadData() {
        adapter = new SuperviseHandleAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(layoutManager);
        mRvList.setAdapter(adapter);

        adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
            @Override
            public void onItemClick(View view, int position) {
                SuperviseHandleBean.ItemsBean itemsBean=adapter.getList().get(position);
                SuperviseHandleDetailActivity.start(SuperviseHandleActivity.this,itemsBean.getId(),itemsBean.getIsRead());
            }
        });
        mRvList.setLoadingListener(new OnRecyclerLoadingListener() {
            @Override
            public void onRefresh() {
                requestData(1);
            }

            @Override
            public void onLoadMore(int reqPage) {
                requestData(reqPage);
            }
        });
        mRvList.refresh();
        //观察督查督办验收
        initObserver();
    }

    /**
     * 观察督查督办验收
     */
    private void initObserver() {
        rxManager = new RxManager();
        rxManager.on(RxConstant.RX_SUPERVISE_ACCEPT, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                requestData(1);
            }
        });
    }

    /**
     * 请求督查督办列表数据
     *
     * @param page
     */
    private void requestData(final int page) {
        final RequestBean request = initRequestParams();
        request.getPageable().setCurrent(page);
        RequestBean.CondBean condBean = new RequestBean.CondBean();
        condBean.setRules(new ArrayList());
        condBean.setGroupOp("AND");
        request.setCond(condBean);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.SUPERVISE_LIST, request, new OKHttpManager.ResultCallback<BaseBean<SuperviseHandleBean >>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
                adapter.setErrorReqList(message, mRvList);
            }

            @Override
            public void onResponse(BaseBean<SuperviseHandleBean> response) {
                hideRDialog();
                SuperviseHandleBean acceptanceBean = response.getBody();
                List<SuperviseHandleBean.ItemsBean> list = null;
                if (acceptanceBean != null) list = acceptanceBean.getItems();
                adapter.setSuccessReqList(list, request.getPageable().getSize(), page, mRvList,"暂无督查督办单");
            }
        }, this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxManager.clear();
        rxManager = null;
    }
}
