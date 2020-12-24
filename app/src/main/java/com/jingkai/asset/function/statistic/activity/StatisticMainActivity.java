package com.jingkai.asset.function.statistic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.callback.OnItemClickRecyclerListener;
import com.jingkai.asset.common.activity.WebActivity;
import com.jingkai.asset.common.helper.CodePermissionHelper;
import com.jingkai.asset.common.helper.PermissionCode;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.operation.adapter.AssetsOperationMainAdapter;
import com.jingkai.asset.function.operation.entity.AssetsOperationBean;
import com.jingkai.asset.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/15 15:22
 * Description:统计分析 资产经营主页面
 */
public class StatisticMainActivity extends BaseActivity {
    @Bind(R.id.rv_assets_operation)
    RecyclerView mRvList;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_assets_operation_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("统计分析");
    }

    @Override
    protected void loadData() {
        String[] nameArr = {"房产出租率", "资产收支统计", "客户行业分析", "资产修缮统计", "督查督办统计"};

        int[] iconArr = {R.mipmap.statistic_chuzulv,//房产出租率
                R.mipmap.statistic_shouzhi,//房产收支统计
                R.mipmap.statistic_hangye,//客户行业分析
                R.mipmap.statistic_xiushan,//资产修缮统计
                R.mipmap.statistic_ducha//督查督办统计
        };
        String[] urlArr = new String[]{
                URLConstant.BASE_HTML_URL + "HouseRental?token=" + SpUtil.getToken(),//房产出租率
                URLConstant.BASE_HTML_URL + "Budget?token=" + SpUtil.getToken(),//房产收支统计
                URLConstant.BASE_HTML_URL + "CustomerAnalysis?token=" + SpUtil.getToken(),//客户行业分析
                URLConstant.BASE_HTML_URL + "AssetRepair?token=" + SpUtil.getToken(),//资产修缮统计
                URLConstant.BASE_HTML_URL + "Supervision?token=" + SpUtil.getToken()//督查督办统计
        };
        //将没有的权限过滤掉
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

        //根据权限拼页面展示的数据
        final List<AssetsOperationBean> list = new ArrayList<>();
        for (int i = 0; i < filterList.size(); i++) {
            AssetsOperationBean bean = new AssetsOperationBean();
            int assetsIndex = 0;//资源位置下标
            PermissionCode code = PermissionCode.getById(filterList.get(i));
            switch (code) {
                //出租率统计
                case STATIC_HOUSE_OCCUPANCY_RATE:
                    assetsIndex = 0;
                    break;
                //房产收支统计
                case STATIC_INCOME_EXPENSES:
                    assetsIndex = 1;
                    break;
                //客户行业分析
                case STATIC_INDUSTRY_ANALYSIS:
                    assetsIndex = 2;
                    break;
                //资产修缮统计
                case STATIC_ASSETS_REPAIR:
                    assetsIndex = 3;
                    break;
                //督察督办统计
                case STATIC_SUPERVISE_EXAMINE:
                    assetsIndex = 4;
                    break;
            }
            bean.setName(nameArr[assetsIndex]);
            bean.setIcon(iconArr[assetsIndex]);
            bean.setData(urlArr[assetsIndex]);
            list.add(bean);
        }
        //加载数据
        setPageData(list);

    }

    /**
     * 渲染页面
     *
     * @param list
     */
    private void setPageData(final List<AssetsOperationBean> list) {
        AssetsOperationMainAdapter adapter = new AssetsOperationMainAdapter(list, this);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mRvList.setLayoutManager(manager);
        mRvList.setAdapter(adapter);

        adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
            @Override
            public void onItemClick(View view, int position) {
                AssetsOperationBean bean = list.get(position);
                Intent intent = new Intent(StatisticMainActivity.this, WebActivity.class);
                intent.putExtra("url", bean.getData());
                startActivity(intent);
            }
        });
    }

}
