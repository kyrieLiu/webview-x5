package com.jingkai.asset.function.operation.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.common.adapter.CommonFragmentPagerAdapter;
import com.jingkai.asset.function.operation.fragment.SuperviseHandleFragment;
import com.jingkai.asset.widget.mark_tablayout.MarkTabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/16 14:21
 * Description:督查督办验收列表页面
 */
public class SuperviseHandleAcceptanceActivity extends BaseActivity {

    @Bind(R.id.mtl_common)
    MarkTabLayout mMtlCommon;
    @Bind(R.id.vp_common)
    ViewPager mVpCommon;


    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.common_tab_viewpager;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("督查督办验收");
    }

    @Override
    protected void loadData() {
        String[] titleArr = {"已验收", "待验收"};
        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < titleArr.length; i++) {
            SuperviseHandleFragment itemFragment = new SuperviseHandleFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("pageIndex", i + 1);
            itemFragment.setArguments(bundle);
            list.add(itemFragment);
        }

        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(this, getSupportFragmentManager(), list,  Arrays.asList(titleArr));
        mVpCommon.setAdapter(adapter);
        mMtlCommon.setupWithViewPager(mVpCommon);
    }
}
