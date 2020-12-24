package com.jingkai.asset.function.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.base.BaseFragment;
import com.jingkai.asset.common.activity.WebActivity;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.main.adapter.PandectListAdapter;
import com.jingkai.asset.function.main.entity.OverviewBean;
import com.jingkai.asset.function.main.entity.PandectBean;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.SpUtil;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.utils.view.RoundedCornersTransformation;
import com.jingkai.asset.utils.view.ViewUtil;
import com.jingkai.asset.widget.MeasureRecyclerView;
import com.jingkai.asset.widget.xrecycler.ItemGridDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liuyin on 2019/2/22 11:14
 *
 * @Describe 总览页面
 */
public class PandectFragment extends BaseFragment {


    @Bind(R.id.rv_pandect_list)
    MeasureRecyclerView mRvList;
    @Bind(R.id.ll_pandect_distribute_top)
    LinearLayout mLlDistributeTop;
    @Bind(R.id.iv_pandect_distribute)
    ImageView mIvDistribute;
    @Bind(R.id.srl_pandect_refresh)
    SmartRefreshLayout mSrlRefresh;

    private List<String> list;

    @Override
    protected int getContentId() {
        return R.layout.fragment_pandect;
    }

    @Override
    protected void initView(Bundle bundle) {
    }

    @Override
    protected void loadData() {
        list = new ArrayList<>();
        requestOverviewData();
        initIntroduceMenu(list);//初始化网格布局

        RequestOptions options = new RequestOptions()
                //.placeholder(R.mipmap.image_default_holder)
                .transforms(new CenterCrop(), new RoundedCornersTransformation(getActivity(), ViewUtil.dp2px(10), 0, RoundedCornersTransformation.CornerType.ALL));
        Glide.with(getActivity()).load(R.mipmap.chanyefenbu).apply(options).into(mIvDistribute);

        mSrlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestOverviewData();
            }
        });

        mRvList.addItemDecoration(new ItemGridDecoration(ViewUtil.dp2px(10), ViewUtil.dp2px(10), 2));

    }

    /**
     * 获取总览数据
     */
    private void requestOverviewData() {
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.OVERVIEW, new HashMap<>(), new OKHttpManager.ResultCallback<BaseBean<OverviewBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
                if (mSrlRefresh.getState() == RefreshState.Refreshing) {
                    mSrlRefresh.finishRefresh();
                }
            }

            @Override
            public void onResponse(BaseBean<OverviewBean> response) {
                hideRDialog();
                if (mSrlRefresh.getState() == RefreshState.Refreshing) {
                    mSrlRefresh.finishRefresh();
                }
                OverviewBean bean = response.getBody();
                if (bean == null) return;
                list.clear();
                list.add(getValue(bean.getAcreageTotal() ) + "万");
                list.add(getValue(bean.getParkCount()));
                list.add(getValue(bean.getHeightEc()) + (" (" + getValue(bean.getHeightEcRate()) + "%)"));
                list.add(getValue(bean.getRentalRate()) + "%");
                list.add(getValue(bean.getNormalEc()));
                initIntroduceMenu(list);
            }
        }, this);
    }

    private String getValue(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        return decimalFormat.format(d);
    }


    /**
     * 初始化网格布局
     *
     * @param textList
     */
    private void initIntroduceMenu(List<String> textList) {
        String menus[] = {"资产面积(m2)", "项目总数(个)", "高新技术企业(家)", "房产出租率", "签约企业(家)"};
        int icons[] = {R.mipmap.pandect_cover_area, R.mipmap.pandect_total_assets, R.mipmap.pandect_innovative_business,
                R.mipmap.pandect_occupancy_rate, R.mipmap.pandect_promote_platform};
        List<PandectBean> list = new ArrayList<>();
        for (int i = 0; i < menus.length; i++) {
            PandectBean model = new PandectBean();
            model.setTitle(menus[i]);
            model.setImage(icons[i]);
            if (textList.size() != 0) {
                model.setData(textList.get(i));
            }
            list.add(model);
        }
        PandectListAdapter adapter = new PandectListAdapter(list, getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRvList.setLayoutManager(layoutManager);
        mRvList.setAdapter(adapter);
    }


    @OnClick({R.id.ll_pandect_distribute_top, R.id.iv_pandect_distribute})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_pandect_distribute_top:
            case R.id.iv_pandect_distribute:
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", URLConstant.BASE_HTML_URL + "AssetMap?token=" + SpUtil.getToken());
                startActivity(intent);
                break;
        }
    }


}
