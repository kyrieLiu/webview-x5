package com.jingkai.asset.function.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.base.BaseFragment;
import com.jingkai.asset.callback.OnItemClickRecyclerListener;
import com.jingkai.asset.common.entity.RequestBean;
import com.jingkai.asset.common.helper.CodePermissionHelper;
import com.jingkai.asset.common.helper.PermissionCode;
import com.jingkai.asset.config.AppConstants;
import com.jingkai.asset.config.RxConstant;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.main.adapter.HomeMenuAdapter;
import com.jingkai.asset.function.main.adapter.HomeWaitDoneAdapter;
import com.jingkai.asset.function.main.entity.HomeMenuBean;
import com.jingkai.asset.function.main.entity.WaitDoneBean;
import com.jingkai.asset.function.manage.activity.AssetManagementActivity;
import com.jingkai.asset.function.operation.activity.AssetsOperationMainActivity;
import com.jingkai.asset.function.repair.activity.RepairProjectDetailActivity;
import com.jingkai.asset.function.statistic.activity.StatisticMainActivity;
import com.jingkai.asset.function.supervise.activity.SuperviseHandleDetailActivity;
import com.jingkai.asset.function.waitdone.WaitDoneListActivity;
import com.jingkai.asset.manager.RxManager;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.GlideImageLoader;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.utils.view.ViewUtil;
import com.jingkai.asset.widget.MeasureRecyclerView;
import com.jingkai.asset.widget.MyScrollView;
import com.jingkai.asset.widget.view.LoadExceptionView;
import com.jingkai.asset.widget.xrecycler.ItemLinearDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by liuyin on 2019/2/21 14:00
 * <p>
 * Describe 首页
 */
public class HomeFragment extends BaseFragment {
    //功能菜单列表
    @Bind(R.id.rv_home_function_menu)
    RecyclerView mRvMenu;
    //刷新控件
    @Bind(R.id.srl_home_refresh)
    SmartRefreshLayout refreshLayout;
    //滚动控件
    @Bind(R.id.my_scrollview_home)
    MyScrollView scrollView;
    //待办事项父布局
    @Bind(R.id.ll_home_wait_done_parent)
    LinearLayout mLlWaitDoneParent;
    //待办事项列表
    @Bind(R.id.rl_wait_done)
    MeasureRecyclerView mRvWaitDone;
    @Bind(R.id.banner)
    Banner mBanner;//banner轮播图
    //待办事项请求返回数据提示
    @Bind(R.id.view_wait_done_warn)
    LoadExceptionView mViewWaitWarn;

    private RxManager rxManager;
    private HomeWaitDoneAdapter<WaitDoneBean.ItemsBean> waitDoneAdapter;

    @Override
    protected int getContentId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(Bundle bundle) {
    }

    @Override
    protected void loadData() {

        initBanner();//初始化banner图

        initFunctionMenu();//初始化功能菜单

        initWaitDoneList();//初始化待办事项列表

        listenScrollView();//监听ScrollView滚动状态

        initObserver();//观察修缮终审和督查督办验收

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestWaitDone();
            }
        });

    }

    /**
     * 初始化功能菜单
     */
    private void initFunctionMenu() {
        String menus[] = {"资产管理", "统计分析", "资产运营"};
        int icons[] = {R.mipmap.asset_management, R.mipmap.asset_operate, R.mipmap.asset_yunying};
        List<HomeMenuBean> list = new ArrayList<>();
        for (int i = 0; i < menus.length; i++) {
            HomeMenuBean model = new HomeMenuBean();
            model.setMenuName(menus[i]);
            model.setIconPath(icons[i]);
            list.add(model);
        }
        HomeMenuAdapter adapter = new HomeMenuAdapter(list, getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mRvMenu.setLayoutManager(layoutManager);
        mRvMenu.setAdapter(adapter);

        adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                switch (position) {
                    case 0://资产管理
                        //如果既没有资产台账也没有设备设施台账权限,就不要点进去了
                        if (!CodePermissionHelper.havePermission(PermissionCode.MANAGE_HOUSE_STANDING_BOOK.getCode())
                                && !CodePermissionHelper.havePermission(PermissionCode.MANAGE_HOUSE_EQUIPMENT_BOOK.getCode())) {
                            ToastUtil.toastShortCenter("暂无权限");
                            return;
                        }
                        intent.setClass(getActivity(), AssetManagementActivity.class);
                        startActivity(intent);
                        break;
                    case 1://统计分析
                        if (!isHaveStatisticalAnalysisPermission()){
                            ToastUtil.toastShortCenter("暂无权限");
                            return;
                        }
                        intent.setClass(getActivity(), StatisticMainActivity.class);
                        startActivity(intent);
                        break;
                    case 2://资产运营
                        if (!isHaveAssetOperationPermission()){
                            ToastUtil.toastShortCenter("暂无权限");
                            return;
                        }
                        intent.setClass(getActivity(), AssetsOperationMainActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    /**
     * 判断统计分析入口权限
     */
    private boolean isHaveStatisticalAnalysisPermission(){

        List<String> permissionList = new ArrayList<String>() {
            {
                add(PermissionCode.STATIC_HOUSE_OCCUPANCY_RATE.getCode());//出租率统计
                add(PermissionCode.STATIC_INCOME_EXPENSES.getCode());//房产收支统计
                add(PermissionCode.STATIC_INDUSTRY_ANALYSIS.getCode());//客户行业分析
                add(PermissionCode.STATIC_ASSETS_REPAIR.getCode());//资产修缮统计
                add(PermissionCode.STATIC_SUPERVISE_EXAMINE.getCode());//督察督办统计
            }
        };

        List<String> filterList = CodePermissionHelper.filterPermissionList(permissionList);
        return filterList.size()>0;
    }

    /**
     * 判断资产运营入口权限
     */
    private boolean isHaveAssetOperationPermission(){
        List<String> permissionList = new ArrayList<String>() {
            {
                add(PermissionCode.REPAIR_PLAN.getCode());//修缮计划单
                add(PermissionCode.REPAIR_Approval.getCode());//修缮审批 zy
                add(PermissionCode.REPAIR_FINAL_JUDGMENT.getCode());//修缮终审
                add(PermissionCode.SUPERVISE_HANDLE.getCode());//督查督办单
                add(PermissionCode.SUPERVISE_CHECK_ACCEPT.getCode());//督查督办验收
                add(PermissionCode.PROPERTY_SERVISE_STANDARDS.getCode());//物业服务标准
                add(PermissionCode.SEE_SUPPLIER.getCode());//查看供应商
            }
        };

        List<String> filterList = CodePermissionHelper.filterPermissionList(permissionList);
        return filterList.size() > 0;
    }


    /**
     * 监听滚动状态
     */
    private void listenScrollView() {
//        mLlTop.setAlpha(0);
//        scrollView.setOnScollChangedListener(new MyScrollView.OnScollChangedListener() {
//            @Override
//            public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
//                float result = y / 400f;
//                if (result >= 1) {
//                    result = 1.0f;
//                }
//                mLlTop.setAlpha(result);
//            }
//        });
    }


    /**
     * 初始化banner图
     */
    private void initBanner() {
        List<Integer> imageURL = new ArrayList<Integer>() {
            {
                add(R.mipmap.banner1);
                add(R.mipmap.banner2);
                add(R.mipmap.banner3);
            }
        };

        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(imageURL);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //ToastUtil.toastShortCenter("position==" + position);
                //H5Activity.start(getActivity(), "详情", "http://wwww.baidu.com");
            }
        });

        mBanner.startAutoPlay();

    }


    /**
     * 初始化待办事项列表
     */
    private void initWaitDoneList() {

        //如果有共享文件权限,展示共享文件入口
        if (!CodePermissionHelper.havePermission(PermissionCode.HOME_WAIT_DONE.getCode())) {
            mLlWaitDoneParent.setVisibility(View.GONE);
            return;
        }else {
            mLlWaitDoneParent.setVisibility(View.VISIBLE);
        }

        waitDoneAdapter = new HomeWaitDoneAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRvWaitDone.setLayoutManager(layoutManager);
        mRvWaitDone.setAdapter(waitDoneAdapter);
        mRvWaitDone.addItemDecoration(new ItemLinearDivider(1, ViewUtil.dp2px(10), ViewUtil.dp2px(10), getResources().getColor(R.color.gray_color)));
        waitDoneAdapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
            @Override
            public void onItemClick(View view, int position) {
                WaitDoneBean.ItemsBean bean = waitDoneAdapter.getList().get(position);
                if ("修缮".equals(bean.getTypeName())) {//修缮项目类型
                    RepairProjectDetailActivity.start(getActivity(), bean.getIdBusiness(), 0, 0,bean.getTypeStatus());
                } else {//督查督办类型
                    SuperviseHandleDetailActivity.start(getActivity(), bean.getIdBusiness(), 1);
                }

            }
        });

        requestWaitDone();


    }

    /**
     * 请求待办事项数据
     */
    private void requestWaitDone() {
        RequestBean requestBean = initRequestParams();

        OKHttpManager.postJsonRequest(URLConstant.WAIT_DONE_HOME, requestBean, new OKHttpManager.ResultCallback<BaseBean<WaitDoneBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                if (refreshLayout.getState() == RefreshState.Refreshing) {
                    refreshLayout.finishRefresh();
                }
                mViewWaitWarn.setVisibility(View.VISIBLE);
                mViewWaitWarn.setExceptionMessage(AppConstants.HTTP_CONNECT_ERROR);
            }

            @Override
            public void onResponse(BaseBean<WaitDoneBean> response) {
                if (refreshLayout.getState() == RefreshState.Refreshing) {
                    refreshLayout.finishRefresh();
                }
                final List<WaitDoneBean.ItemsBean> list = response.getBody().getItems();
                if (list == null || list.size() == 0) {
                    mViewWaitWarn.setVisibility(View.VISIBLE);
                    mViewWaitWarn.setExceptionMessage("暂无待办事项");
                    mRvWaitDone.setVisibility(View.GONE);
                } else {
                    mViewWaitWarn.setVisibility(View.GONE);
                    mRvWaitDone.setVisibility(View.VISIBLE);
                }
                waitDoneAdapter.setList(list);
            }
        }, this);
    }

    /**
     * 观察督查督办验收和修缮审核
     */
    private void initObserver() {
        rxManager = new RxManager();
        rxManager.on(RxConstant.RX_SUPERVISE_ACCEPT, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                requestWaitDone();
            }
        });
        rxManager.on(RxConstant.RX_REPAIR_CHECK, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                requestWaitDone();
            }
        });
    }

    @OnClick({R.id.ll_home_wait_done_more})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_home_wait_done_more:
                Intent intent = new Intent(getActivity(), WaitDoneListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rxManager.clear();
        rxManager = null;

        //结束轮播
        if (mBanner != null) {
            mBanner.stopAutoPlay();
        }
    }
}
