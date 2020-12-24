package com.jingkai.asset.function.operation.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.common.adapter.ParentTreeListViewAdapter;
import com.jingkai.asset.common.entity.Node;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.operation.adapter.SupplierTreeAdapter;
import com.jingkai.asset.function.operation.entity.SupplierBean;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.PermissionHelper;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.widget.dialog.SelectMenuDialog;
import com.jingkai.asset.widget.view.LoadExceptionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/17 10:11
 * Describe: 查看供应商树状图页面
 */
public class SupplierActivity extends BaseActivity {

    //供应商树状图
    @Bind(R.id.lv_supplier_list)
    ListView mLvSupplier;

    @Bind(R.id.lev_supplier)
    LoadExceptionView exceptionView;


    private SupplierTreeAdapter<Node> adapter;
    private List<Node> mDatas = new ArrayList<Node>();

    private PermissionHelper mHelper;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_supplier;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("查看供应商");
    }

    @Override
    protected void loadData() {
        requestData();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.SUPPLIER_LIST, new HashMap<>(), new OKHttpManager.ResultCallback<BaseBean<SupplierBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
                exceptionView.setVisibility(View.VISIBLE);
                exceptionView.setExceptionMessage(message);
            }

            @Override
            public void onResponse(BaseBean<SupplierBean> response) {
                hideRDialog();
                List<SupplierBean.CompanyListBean> companyList = response.getBody().getCompanyList();
                if (companyList != null && companyList.size() > 0) {
                    setData(companyList);
                } else {
                    exceptionView.setVisibility(View.VISIBLE);
                    exceptionView.setExceptionMessage("暂无供应商信息");
                }
            }
        }, this);
    }

    /**
     * 加载供应商列表数据
     *
     * @param companyList
     */
    private void setData(List<SupplierBean.CompanyListBean> companyList) {
        mDatas.clear();
        for (int i = 0; i < companyList.size(); i++) {
            SupplierBean.CompanyListBean companyBean = companyList.get(i);
            Node node = new Node();
            node.setpId("");
            node.setName(companyBean.getDepartName());
            node.setShowID(String.valueOf(i) + 0);
            mDatas.add(node);
            List<SupplierBean.CompanyListBean.ProfessionalListBean> professionalList = companyList.get(i).getProfessionalList();
            if (professionalList != null && professionalList.size() > 0) {
                for (int j = 0; j < professionalList.size(); j++) {
                    SupplierBean.CompanyListBean.ProfessionalListBean professionalBean = professionalList.get(j);
                    Node secondNode = new Node();
                    secondNode.setpId(node.getShowID());
                    secondNode.setShowID(i + String.valueOf(j) + 1);
                    secondNode.setName(professionalBean.getProfessionalFieldTitle());
                    secondNode.setRoleLevel(1);
                    List<SupplierBean.CompanyListBean.ProfessionalListBean.SupplierCompanyListBean> supplierList = professionalBean.getSupplierCompanyList();
                    if (supplierList != null) {
                        secondNode.setAllCount(supplierList.size());
                        mDatas.add(secondNode);
                    }
                    if (supplierList != null && supplierList.size() > 0) {
                        for (int k = 0; k < supplierList.size(); k++) {
                            SupplierBean.CompanyListBean.ProfessionalListBean.SupplierCompanyListBean supplierBean = supplierList.get(k);
                            Node thirdNode = new Node();
                            thirdNode.setpId(secondNode.getShowID());
                            thirdNode.setShowID(i + String.valueOf(j) + k + 2);
                            thirdNode.setName(supplierBean.getSupplierName());
                            thirdNode.setData(supplierBean.getContactPhone());
                            thirdNode.setRoleLevel(2);
                            mDatas.add(thirdNode);
                        }

                    }
                }
            }
        }
        loadTreeData();
    }


    /**
     * 加载树列表
     */
    private void loadTreeData() {
        try {
            adapter = new SupplierTreeAdapter<Node>(mLvSupplier, this,
                    mDatas, 1);
            adapter.setOnTreeNodeClickListener(new ParentTreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(Node node, int position) {
                    showCallDialog(node);

                }

                @Override
                public void onCheckChange(Node node, int position,
                                          List<Node> checkedNodes) {

                }
            });

            mLvSupplier.setAdapter(adapter);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拨打电话
     *
     * @param node
     */
    private void showCallDialog(final Node node) {
        final SelectMenuDialog dialog = new SelectMenuDialog(this);
        dialog.setListener(new SelectMenuDialog.OnMenuSelectListener() {
            @Override
            public void callback() {
                dialog.dismiss();
                requestPermission(node.getData());
            }
        });
        dialog.show();
        dialog.setTitle(node.getName());
        dialog.setContent("供应商热线:"+node.getData());
    }

    private void requestPermission(final String phone) {
        mHelper = new PermissionHelper(this);
        mHelper.requestPermissions("请授予[电话]权限，否则无法拨打电话",
                new PermissionHelper.PermissionListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void doAfterGrand(String... permission) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + phone);
                        intent.setData(data);
                        startActivity(intent);
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        mHelper.againWarnRequestPermission(getResources().getString(R.string.location_permission_hint), SupplierActivity.this);
                    }
                }, Manifest.permission.CALL_PHONE);
    }


    //授权回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mHelper != null)
            mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
