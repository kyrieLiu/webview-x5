package com.jingkai.asset.function.manage.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseFragment;
import com.jingkai.asset.base.BaseListBean;
import com.jingkai.asset.common.adapter.ParentTreeListViewAdapter;
import com.jingkai.asset.common.entity.Node;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.manage.activity.FacilityInformationActivity;
import com.jingkai.asset.function.manage.adapter.EquipmentTreeListViewAdapter;
import com.jingkai.asset.function.manage.entity.EquipmentTreeBean;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/12 10:13
 * Description:设备台账
 */
public class FacilityStandingBookFragment extends BaseFragment {
    @Bind(R.id.lv_device_standing_detail_list)
    ListView mLvDevice;

    private EquipmentTreeListViewAdapter<Node> adapter;
    private List<Node> mDatas = new ArrayList<Node>();

    private Context context;


    public static FacilityStandingBookFragment getInstance(int id) {
        FacilityStandingBookFragment fragment = new FacilityStandingBookFragment();
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
    protected int getContentId() {
        return R.layout.fragment_facility_standing_book;
    }

    @Override
    protected void initView(Bundle bundle) {

    }

    @Override
    protected void loadData() {
        int projectId = getArguments().getInt("id");
        getTreeData(projectId);
    }

    /**
     * 更新园区后刷新数据
     *
     * @param id
     */
    public void updateParkData(int id) {
        getTreeData(id);
    }


    private void getTreeData(int id) {
        params.clear();
        params.put("parkId", id);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.EQUIPMENT_TREE, params, new OKHttpManager.ResultCallback<BaseListBean<EquipmentTreeBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseListBean<EquipmentTreeBean> response) {
                hideRDialog();
                List<EquipmentTreeBean> list = response.getBody();
                initTree(list);
            }
        }, this);
    }


    /**
     * 加载树状图数据
     *
     * @param list
     */
    private void initTree(List<EquipmentTreeBean> list) {
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

    private void filterData(List<EquipmentTreeBean> list, String pid) {
        for (EquipmentTreeBean bean : list) {
            String childId = String.valueOf(pid) + String.valueOf(bean.getId());
            bean.setShowID(childId);
            if (bean.getChildren() != null && bean.getChildren().size() > 0) {
                filterData(bean.getChildren(), bean.getShowID());
            }
            Node node = new Node();
            node.setName(bean.getText());
            node.setpId(pid);
            node.setShowID(childId);
            if (bean.getAttr() != null) {
                if (bean.getAttr().isEquipment()) {
                    node.setRoleLevel(2);
                }
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
            adapter = new EquipmentTreeListViewAdapter<Node>(mLvDevice, context,
                    mDatas, 1);
            adapter.setOnTreeNodeClickListener(new ParentTreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(Node node, int position) {

                    Intent intent = new Intent(getActivity(), FacilityInformationActivity.class);
                    intent.putExtra("id", node.getId());
                    startActivity(intent);

                }

                @Override
                public void onCheckChange(Node node, int position,
                                          List<Node> checkedNodes) {

                }
            });

            mLvDevice.setAdapter(adapter);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
