package com.jingkai.asset.function.property.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.callback.OnItemClickRecyclerListener;
import com.jingkai.asset.common.entity.RequestBean;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.operation.adapter.PropertyServiceStandardAdapter;
import com.jingkai.asset.function.operation.entity.PropertyServiceStandardBean;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.widget.xrecycler.XRecyclerView;

import java.util.List;

import butterknife.Bind;

/**
 * Description:物业服务标准管理搜索页面
 */
public class PropertyServiceStandardSearchActivity extends BaseActivity {
    @Bind(R.id.rv_common_list)
    XRecyclerView mRvList;
    @Bind(R.id.et_search)
    EditText mEtSearch;

    private String parkId;

    private PropertyServiceStandardAdapter<PropertyServiceStandardBean.ItemsBean> adapter;

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

        parkId = getIntent().getStringExtra("parkId");

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
                PropertyWebActivity.start(PropertyServiceStandardSearchActivity.this, itemsBean.getDocUrl(), "物业服务管理标准",itemsBean.getDocId()+"");
            }
        });
        mRvList.setLoadingMoreEnabled(false);
        mRvList.setPullRefreshEnabled(false);
    }

    /**
     * 获取数据
     *
     * @param page
     * @param searchStr
     */
    private void requestData(final int page, String searchStr) {
        final RequestBean request = initRequestParams();
        request.getPageable().setCurrent(page);
        request.getPageable().setSize(100000);
        RequestBean.EntityBean entityBean = new RequestBean.EntityBean();
        entityBean.setTitle(searchStr);
        if (!TextUtils.isEmpty(parkId) && !("0".equals(parkId))) {
            entityBean.setAcParkId(parkId);
        }
        request.setEntity(entityBean);
        OKHttpManager.postJsonRequest(URLConstant.GET_DOC_LIST, request, new OKHttpManager.ResultCallback<BaseBean<PropertyServiceStandardBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseBean<PropertyServiceStandardBean> response) {
                final List<PropertyServiceStandardBean.ItemsBean> list = response.getBody().getItems();
                adapter.setList(list);
            }
        }, this);
    }
}
