package com.jingkai.asset.function.repair.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.common.adapter.CommonFragmentPagerAdapter;
import com.jingkai.asset.function.repair.fragment.ExamineApproveFragment;
import com.jingkai.asset.widget.mark_tablayout.MarkTabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/16 11:24
 * Description:修缮终审页面
 */
public class RepairFinalJudgmentActivity extends BaseActivity {
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
        setTitle("修缮终审");
    }

    @Override
    protected void loadData() {
        String[] titleArr = {"待终审","已终审"};
        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < titleArr.length; i++) {
            ExamineApproveFragment itemFragment = new ExamineApproveFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("pageIndex", i + 1);
            itemFragment.setArguments(bundle);
            list.add(itemFragment);
        }

        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(this, getSupportFragmentManager(), list, Arrays.asList(titleArr));
        mVpCommon.setAdapter(adapter);
        mMtlCommon.setupWithViewPager(mVpCommon);
    }

}
