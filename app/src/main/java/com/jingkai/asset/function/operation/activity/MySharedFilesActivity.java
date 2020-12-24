package com.jingkai.asset.function.operation.activity;

import android.Manifest;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.base.BaseListBean;
import com.jingkai.asset.callback.OnItemClickRecyclerListener;
import com.jingkai.asset.callback.OnRecyclerLoadingListener;
import com.jingkai.asset.common.entity.RequestBean;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.operation.adapter.MySharedFilesAdapter;
import com.jingkai.asset.function.operation.entity.ShareFilterBean;
import com.jingkai.asset.function.operation.entity.SharedFilesBean;
import com.jingkai.asset.function.repair.entity.FileBean;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.FileUtil;
import com.jingkai.asset.utils.PermissionHelper;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.utils.view.ViewUtil;
import com.jingkai.asset.widget.popup.ShareFileFilterPop;
import com.jingkai.asset.widget.xrecycler.ItemLinearDivider;
import com.jingkai.asset.widget.xrecycler.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的共享文件
 */
public class MySharedFilesActivity extends BaseActivity {

    @Bind(R.id.rv_common_list)
    XRecyclerView mRvList;

    @Bind(R.id.tv_right)
    TextView mTvRight;

    private PermissionHelper mHelper;

    private MySharedFilesAdapter<SharedFilesBean.ItemsBean> mAdapter;
    private ShareFileFilterPop filterPop;
    private List<ShareFilterBean> filterList;
    //筛选条件的code值
    private String filterCode;

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
        setTitle("共享文件");
        rightVisible("筛选");
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
        mAdapter = new MySharedFilesAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(layoutManager);
        mRvList.setAdapter(mAdapter);
        mRvList.addItemDecoration(new ItemLinearDivider(1, ViewUtil.dp2px(10), ViewUtil.dp2px(10), getResources().getColor(R.color.gray_color)));
        mAdapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
            @Override
            public void onItemClick(View view, int position) {
                SharedFilesBean.ItemsBean bean = mAdapter.getList().get(position);
                if (TextUtils.isEmpty(bean.getFileAddr())) {
                    ToastUtil.toastShortCenter("暂无文件");
                } else {
                    requestPermission(bean);
                }

            }
        });

        mRvList.refresh();

    }

    /**
     * 获取权限
     */
    private void requestPermission(final SharedFilesBean.ItemsBean bean) {
        if (mHelper == null) mHelper = new PermissionHelper(this);
        mHelper.requestPermissions(getString(R.string.voice_play_hint),
                new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        FileBean fileBean = new FileBean();
                        fileBean.setAddr(bean.getFileAddr());
                        fileBean.setName(bean.getFileSharingTypeTitle());
                        FileUtil.getInstance().openFile(fileBean, MySharedFilesActivity.this);
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        mHelper.againWarnRequestPermission(getString(R.string.image_permission_hint), MySharedFilesActivity.this);
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * 获取共享文件数据
     *
     * @param page
     */
    private void requestData(final int page) {
        final RequestBean request = initRequestParams();
        request.getPageable().setCurrent(page);
        RequestBean.CondBean condBean = request.getCond();
        final HashMap<String, String> map = new HashMap<>();
        map.put("field", "fileSharingType");
        map.put("data", filterCode);
        map.put("op", "eq");
        List<HashMap> list = new ArrayList<HashMap>() {{
            add(map);
        }};
        condBean.setGroupOp("AND");
        condBean.setRules(list);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.SHARED_FILES_LIST, request, new OKHttpManager.ResultCallback<BaseBean<SharedFilesBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
                mAdapter.setErrorReqList(message, mRvList);
            }

            @Override
            public void onResponse(BaseBean<SharedFilesBean> response) {
                hideRDialog();
                final List<SharedFilesBean.ItemsBean> list = response.getBody().getItems();
                mAdapter.setSuccessReqList(list, request.getPageable().getSize(), page, mRvList,"暂无共享文件");

            }
        }, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mHelper != null)
            mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick({R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            //筛选数据
            case R.id.tv_right:
                if (filterPop == null) {
                    getFilterList();
                } else {
                    filterPop.showAsDropDown(mTvRight, 0, ViewUtil.dp2px(-15));
                }
                break;
        }
    }


    /**
     * 获取筛选数据
     */
    private void getFilterList() {
        showRDialog();
        OKHttpManager.postAsyn(URLConstant.SHARED_FILES_FILTER_LIST, params, new OKHttpManager.ResultCallback<BaseListBean<ShareFilterBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseListBean<ShareFilterBean> response) {
                hideRDialog();
                filterList = response.getBody();
                if (filterList != null && filterList.size() > 0) {

                    showMenuFilter(filterList);
                } else {
                    ToastUtil.toastShortCenter("暂无筛选条件");
                }


            }
        }, this);
    }

    /**
     * 展示筛选框
     *
     * @param list
     */
    private void showMenuFilter(List<ShareFilterBean> list) {

        if (filterPop == null) {
            filterPop = new ShareFileFilterPop(this);
            filterPop.setBackgroundDrawable(new BitmapDrawable());
            filterPop.setData(list);
            filterPop.setListener(new ShareFileFilterPop.OnFilterSelectListener() {
                @Override
                public void callback(ShareFilterBean bean) {
                    filterCode = bean.getData();
                    requestData(1);
                }
            });
        }

        filterPop.showAsDropDown(mTvRight, 0, ViewUtil.dp2px(-15));

    }

}
