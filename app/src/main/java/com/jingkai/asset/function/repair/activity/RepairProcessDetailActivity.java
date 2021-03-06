package com.jingkai.asset.function.repair.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.function.repair.adapter.RepairDetailProcessAdapter;
import com.jingkai.asset.function.repair.entity.FileBean;
import com.jingkai.asset.function.repair.entity.ProcessCacheBean;
import com.jingkai.asset.function.repair.entity.RepairDetailBean;
import com.jingkai.asset.utils.FileUtil;
import com.jingkai.asset.utils.PermissionHelper;
import com.jingkai.asset.utils.view.ViewUtil;
import com.jingkai.asset.widget.xrecycler.ItemLinearDivider;
import com.jingkai.asset.widget.xrecycler.XRecyclerView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/9 16:04
 * Describe: 修缮审核过程详情
 */
public class RepairProcessDetailActivity extends BaseActivity {
    //列表
    @Bind(R.id.rv_common_list)
    XRecyclerView mRvList;

    private List<RepairDetailBean.ProcessBean> list;
    private RepairDetailProcessAdapter adapter;

    //申请权限
    private PermissionHelper mHelper;

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
        setTitle("操作过程详情");
        list = ProcessCacheBean.processList;

    }

    @Override
    protected void loadData() {
        mRvList.setLoadingMoreEnabled(false);
        mRvList.setPullRefreshEnabled(false);
        adapter = new RepairDetailProcessAdapter(list, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(layoutManager);
        mRvList.setAdapter(adapter);
        mRvList.addItemDecoration(new ItemLinearDivider(ViewUtil.dp2px(10), 0, 0, getResources().getColor(R.color.gray_light_color)));

        //设置打开文件回调
        adapter.setOpenFileListener(new RepairDetailProcessAdapter.OpenFileListener() {
            @Override
            public void openFile(FileBean fileBean) {
                requestFilePermission(fileBean);
            }
        });

    }

    /**
     * 获取权限
     *
     * @param fileBean
     */
    private void requestFilePermission(final FileBean fileBean) {
        if (mHelper == null) mHelper = new PermissionHelper(this);
        mHelper.requestPermissions(RepairProcessDetailActivity.this.getString(R.string.voice_play_hint),
                new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        FileUtil.getInstance().openFile(fileBean, RepairProcessDetailActivity.this);
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        mHelper.againWarnRequestPermission(RepairProcessDetailActivity.this.getString(R.string.image_permission_hint), RepairProcessDetailActivity.this);
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    //授权回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mHelper != null)
            mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
