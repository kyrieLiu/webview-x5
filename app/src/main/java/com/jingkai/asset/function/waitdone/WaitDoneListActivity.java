package com.jingkai.asset.function.waitdone;

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
import com.jingkai.asset.function.main.adapter.HomeWaitDoneAdapter;
import com.jingkai.asset.function.main.entity.WaitDoneBean;
import com.jingkai.asset.function.repair.activity.RepairProjectDetailActivity;
import com.jingkai.asset.function.supervise.activity.SuperviseHandleDetailActivity;
import com.jingkai.asset.manager.RxManager;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.utils.view.ViewUtil;
import com.jingkai.asset.widget.xrecycler.ItemLinearDivider;
import com.jingkai.asset.widget.xrecycler.XRecyclerView;

import java.util.List;

import butterknife.Bind;
import rx.functions.Action1;

public class WaitDoneListActivity extends BaseActivity {

    @Bind(R.id.rv_common_list)
    XRecyclerView mRvList;
    private RxManager rxManager;

    private HomeWaitDoneAdapter<WaitDoneBean.ItemsBean> waitDoneAdapter;

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

        setTitle("待办事项列表");


    }

    @Override
    protected void loadData() {

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
        waitDoneAdapter = new HomeWaitDoneAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(layoutManager);
        mRvList.setAdapter(waitDoneAdapter);
        mRvList.addItemDecoration(new ItemLinearDivider(1, ViewUtil.dp2px(10), ViewUtil.dp2px(10), getResources().getColor(R.color.gray_color)));
        waitDoneAdapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
            @Override
            public void onItemClick(View view, int position) {
                WaitDoneBean.ItemsBean bean = waitDoneAdapter.getList().get(position);
                if ("修缮".equals(bean.getTypeName())) {//修缮项目类型
                    RepairProjectDetailActivity.start(WaitDoneListActivity.this, bean.getIdBusiness(), 0, 1,bean.getTypeStatus());
                } else {//督查督办类型
                    SuperviseHandleDetailActivity.start(WaitDoneListActivity.this, bean.getIdBusiness(), 1);
                }
            }
        });
        mRvList.refresh();

        //观察督查督办验收和修缮审核
        initObserver();
    }

    /**
     * 观察督查督办验收和修缮审核
     */
    private void initObserver() {
        rxManager = new RxManager();
        rxManager.on(RxConstant.RX_SUPERVISE_ACCEPT, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                requestData(1);
            }
        });
        rxManager.on(RxConstant.RX_REPAIR_CHECK, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                requestData(1);
            }
        });
    }

    /**
     * 请求待办事项列表数据
     *
     * @param page
     */
    private void requestData(final int page) {
        final RequestBean request = initRequestParams();
        request.getPageable().setCurrent(page);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.WAIT_DONE_MORE, request, new OKHttpManager.ResultCallback<BaseBean<WaitDoneBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
                waitDoneAdapter.setErrorReqList(message, mRvList);
            }

            @Override
            public void onResponse(BaseBean<WaitDoneBean> response) {
                hideRDialog();
                final List<WaitDoneBean.ItemsBean> list = response.getBody().getItems();
                waitDoneAdapter.setSuccessReqList(list, request.getPageable().getSize(), page, mRvList, "暂无待办事项");
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
