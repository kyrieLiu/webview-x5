package com.jingkai.asset.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jingkai.asset.common.entity.RequestBean;
import com.jingkai.asset.config.AppConstants;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.LoadingDialogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * Created by liuyin on 2019/3/14 14:18
 * Describe: 基类Frgment
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;
    protected View rootView;
    protected HashMap<String, Object> params = new HashMap<>();//用于存储请求参数


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getContentId(), container, false);
        ButterKnife.bind(this, rootView);
        initView(getArguments());
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected abstract int getContentId();

    protected abstract void initView(Bundle bundle);

    protected abstract void loadData();


    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void showRDialog() {

        LoadingDialogUtils.getUtils().showProgressDialog(getActivity());

    }

    public void hideRDialog() {
        LoadingDialogUtils.getUtils().dismissDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LoadingDialogUtils.getUtils().dismissDialog();
        OKHttpManager.cancelTag(this);
    }

    /**
     * 初始化请求参数
     */
    protected RequestBean initRequestParams(){
        RequestBean  requestBean=new RequestBean();
        RequestBean.CondBean condBean=new RequestBean.CondBean();
        condBean.setGroups(new ArrayList());
        condBean.setRules(new ArrayList());
        requestBean.setCond(condBean);
        RequestBean.PageableBean pageableBean=new RequestBean.PageableBean();
        requestBean.setPageable(pageableBean);
        return requestBean;
    }

    /**
     * 清理临时压缩的图片和语音
     */
    public void deleteCacheDir() {
        File file = new File(Environment.getExternalStorageDirectory(), AppConstants.SD_PATH);
        deleteDirWihtFile(file);
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

}
