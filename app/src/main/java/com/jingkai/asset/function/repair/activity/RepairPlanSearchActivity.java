package com.jingkai.asset.function.repair.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.callback.OnItemClickRecyclerListener;
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
import rx.functions.Action1;

/**
 * Description:物业服务标准管理搜索页面
 */
public class RepairPlanSearchActivity extends BaseActivity {
    @Bind(R.id.rv_common_list)
    XRecyclerView mRvList;
    @Bind(R.id.et_search)
    EditText mEtSearch;

    private RxManager rxManager;

    private RepairPlanOrderAdapter<RepairPlanOrderBean.ItemsBean> adapter;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        //搜索框监听
        searchListener();
    }

    private void searchListener() {
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchStr = charSequence.toString().trim();
                requestData(1, searchStr);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0); //强制隐藏键盘
                    requestData(1, mEtSearch.getText().toString());
                    return true;
                }
                return false;

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
                RepairProjectDetailActivity.start(RepairPlanSearchActivity.this, bean.getId(), 1, 0,bean.getRepairStatusTitle());
            }
        });
        mRvList.setLoadingMoreEnabled(false);
        mRvList.setPullRefreshEnabled(false);

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
                requestData(1, "");
            }
        });
    }

    /**
     * 获取数据
     *
     * @param page
     * @param searchStr
     */
    private void requestData(final int page, String searchStr) {
        final RequestBean request = new RequestBean();
        RequestBean.PageableBean pageableBean = new RequestBean.PageableBean();
        pageableBean.setCurrent(page);
        pageableBean.setSize(100000);
        request.setPageable(pageableBean);
        RequestBean.EntityBean entityBean = new RequestBean.EntityBean();
        entityBean.setCustomQuery(searchStr);
        request.setEntity(entityBean);
        OKHttpManager.postJsonRequest(URLConstant.ASSETS_REPAIR_LIST, request, new OKHttpManager.ResultCallback<BaseBean<RepairPlanOrderBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseBean<RepairPlanOrderBean> response) {
                final List<RepairPlanOrderBean.ItemsBean> list = response.getBody().getItems();
                adapter.setList(list);
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
