package com.jingkai.asset.function.manage.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.base.BaseFragment;
import com.jingkai.asset.base.BaseListBean;
import com.jingkai.asset.common.adapter.ParentTreeListViewAdapter;
import com.jingkai.asset.common.entity.Node;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.manage.activity.HousingDetailActivity;
import com.jingkai.asset.function.manage.adapter.AssetsTreeListViewAdapter;
import com.jingkai.asset.function.manage.entity.AssetsRentRateBean;
import com.jingkai.asset.function.manage.entity.HouseTreeBean;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.widget.view.PieChartView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/11 16:15
 * Describe: 资产台账
 */
public class AssetStandingBookFragment extends BaseFragment {
    //出租率报表
    @Bind(R.id.cpv_standbook_occupancy_rate)
    PieChartView mCpvOccupancyRate;
    //空置率报表
    @Bind(R.id.cpv_standbook_vacancy_rate)
    PieChartView mCpvVacancyRate;
    //楼栋房间树状图
    @Bind(R.id.lv_standing_detail_list)
    ListView mLvHouse;
    //房间树数据
    private List<Node> mDatas = new ArrayList<Node>();

    private AssetsTreeListViewAdapter<Node> adapter;
    private Context context;


    @Override
    protected int getContentId() {
        return R.layout.fragment_standing_book;
    }

    @Override
    protected void initView(Bundle bundle) {

    }

    public static AssetStandingBookFragment getInstance(int id) {
        AssetStandingBookFragment fragment = new AssetStandingBookFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    protected void loadData() {
        int projectId = getArguments().getInt("id");

        getRentRate(projectId);//获取出租空置率统计

        getTreeData(projectId);//获取房间树数据

    }

    /**
     * 更新园区后刷新数据
     *
     * @param id
     */
    public void updateParkData(int id) {
        getRentRate(id);//获取出租空置率统计

        getTreeData(id);//获取房间树数据
    }


    /**
     * 请求出租空置率统计
     */
    private void getRentRate(int id) {
        params.clear();
        params.put("parkId", id);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.ASSETS_RENT_RATE, params, new OKHttpManager.ResultCallback<BaseBean<AssetsRentRateBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseBean<AssetsRentRateBean> response) {
                hideRDialog();
                AssetsRentRateBean bean = response.getBody();

                mCpvOccupancyRate.update(Double.parseDouble(bean.getRentalRate()), 0, 1000);

                //mCpvVacancyRate.update(Double.parseDouble(bean.getEmptyRate()), 0, 1000);


            }
        }, this);
    }


    /**
     * 请求房间树数据
     */
    private void getTreeData(int id) {
        params.clear();
        params.put("parkId", id);
        OKHttpManager.postJsonRequest(URLConstant.ASSETS_TREE, params, new OKHttpManager.ResultCallback<BaseListBean<HouseTreeBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseListBean<HouseTreeBean> response) {
                List<HouseTreeBean> list = response.getBody();
                initTree(list);
            }
        }, this);
    }

    /**
     * 加载树状图数据
     *
     * @param list
     */
    private void initTree(List<HouseTreeBean> list) {
        if (list == null) return;
        mDatas.clear();
        filterData(list, "");
        loadTreeData();

    }

    /**
     * 递归形式取数据
     *
     * @param list 当前子List
     * @param pid  父级ID
     */

    private void filterData(List<HouseTreeBean> list, String pid) {
        for (HouseTreeBean bean : list) {
            String childId = String.valueOf(pid) + String.valueOf(bean.getId());
            bean.setShowID(childId);
            if (bean.getChildren() != null && bean.getChildren().size() > 0) {
                filterData(bean.getChildren(), bean.getShowID());
            }
            Node node = new Node();
            node.setName(bean.getText());
            node.setpId(pid);
            node.setShowID(childId);
            HouseTreeBean.AttrBean attr=bean.getAttr();
            if (attr != null) {
                if (attr.isIsAssetProperty()) {
                    node.setRoleLevel(5);
                    node.setData(attr.getStatusName());
                }else{
                    node.setRoleLevel(attr.getLevel());
                }
                node.setAllCount(attr.getAssetPropertyCount());
            }
            node.setId(Integer.parseInt(bean.getId()));

            mDatas.add(node);
        }
    }


    /**
     * 加载房间树
     */
    private void loadTreeData() {
        try {
            adapter = new AssetsTreeListViewAdapter<Node>(mLvHouse, context,
                    mDatas, 1);
            adapter.setOnTreeNodeClickListener(new ParentTreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(Node node, int position) {

                    Intent intent = new Intent(getActivity(), HousingDetailActivity.class);
                    intent.putExtra("id", node.getId());
                    intent.putExtra("status", node.getData());
                    startActivity(intent);

                }

                @Override
                public void onCheckChange(Node node, int position,
                                          List<Node> checkedNodes) {

                }
            });

            mLvHouse.setAdapter(adapter);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
