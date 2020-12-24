package com.jingkai.asset.function.main.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.common.helper.CodePermissionHelper;
import com.jingkai.asset.common.helper.PermissionCode;
import com.jingkai.asset.function.login.activity.LoginActivity;
import com.jingkai.asset.function.main.fragment.HomeFragment;
import com.jingkai.asset.function.main.fragment.MineFragment;
import com.jingkai.asset.function.main.fragment.PandectFragment;
import com.jingkai.asset.manager.ActivitiesManager;
import com.jingkai.asset.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liuyin on 2019/2/15 17:46
 *
 * @Describe 首页布局
 */
public class MainActivity extends BaseActivity {
    //fragment容器
    @Bind(R.id.fl_home_container)
    FrameLayout mFlContainer;
    //首页
    @Bind(R.id.rb_tab_home)
    RadioButton mRbHome;
    //总览
    @Bind(R.id.rb_tab_discover)
    RadioButton mRbDiscover;
    //我的
    @Bind(R.id.rb_tab_mine)
    RadioButton mRbMine;
    //控制器容器
    @Bind(R.id.rg_tab)
    RadioGroup mRgTab;
    private long exitTime = 0;

    private List<Fragment> mFragments;//存放需要加载的fragment

    private int mIndex;

    //是否有资产总览权限
    private boolean havePandectPermission;


    @Override
    protected void back() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            ActivitiesManager.getInstance().popAllActivity();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {


        if (!SpUtil.getIsLogin()) {
            LoginActivity.start(this);
        }

        //SpUtil.setToken("customer95478d66d8b54f2fb1a9f2861a927fa4");

        //将导航栏设置为透明色
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }


    @Override
    protected void loadData() {
        mFragments = new ArrayList<>();
        //首页
        HomeFragment homeFragment = new HomeFragment();
        mFragments.add(homeFragment);

        //判断是否有资产总览权限
        if (CodePermissionHelper.havePermission(PermissionCode.ASSET_PANDECT.getCode())) {
            havePandectPermission = true;
            //总览
            PandectFragment pandectFragment = new PandectFragment();
            mFragments.add(pandectFragment);
        } else {
            mRbDiscover.setVisibility(View.GONE);
        }

        //我的
        MineFragment mineFragment = new MineFragment();
        mFragments.add(mineFragment);

        //开启事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //添加首页
        ft.add(R.id.fl_home_container, homeFragment).commit();
        setIndexSelected(0);
    }

    @OnClick({R.id.rb_tab_home, R.id.rb_tab_discover, R.id.rb_tab_mine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_tab_home:
                setIndexSelected(0);
                break;
            case R.id.rb_tab_discover:
                setIndexSelected(1);
                break;
            case R.id.rb_tab_mine:
                //如果没有总览权限,个人中心页面的下标为1
                if (havePandectPermission) {
                    setIndexSelected(2);
                } else {
                    setIndexSelected(1);
                }
                break;
        }
    }

    /**
     * 切换Fragment
     *
     * @param index
     */
    private void setIndexSelected(int index) {
        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        //隐藏
        ft.hide(mFragments.get(mIndex));
        //判断是否添加
        if (!mFragments.get(index).isAdded()) {
            ft.add(R.id.fl_home_container, mFragments.get(index)).show(mFragments.get(index));
        } else {
            ft.show(mFragments.get(index));
        }

        ft.commit();
        //再次赋值
        mIndex = index;

    }


}
