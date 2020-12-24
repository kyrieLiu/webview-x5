package com.jingkai.asset.function.operation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.callback.OnItemClickRecyclerListener;
import com.jingkai.asset.common.helper.CodePermissionHelper;
import com.jingkai.asset.common.helper.PermissionCode;
import com.jingkai.asset.function.operation.adapter.AssetsOperationMainAdapter;
import com.jingkai.asset.function.operation.entity.AssetsOperationBean;
import com.jingkai.asset.function.property.activity.PropertyServiceStandardActivity;
import com.jingkai.asset.function.repair.activity.RepairApprovalActivity;
import com.jingkai.asset.function.repair.activity.RepairFinalJudgmentActivity;
import com.jingkai.asset.function.repair.activity.RepairPlanOrderActivity;
import com.jingkai.asset.function.supervise.activity.SuperviseHandleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/15 15:22
 * Description:资产运营菜单列表页面
 */
public class AssetsOperationMainActivity extends BaseActivity {
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
        setTitle("资产运营");
    }

    @Override
    protected void loadData() {
        String[] nameArr = {"修缮计划单", "修缮审批","修缮终审", "督查督办单", "督查督办验收", "物业服务标准", "查看供应商"};
        int[] iconArr = {R.mipmap.assets_xiushanjihuadan,//修缮计划单
                R.mipmap.assets_xiushanshenpi,//修缮审批
                R.mipmap.assets_xiushanzhongshen,//修缮终审
                R.mipmap.assets_duchadubandan,//督查督办单
                R.mipmap.assets_duchadubanyanshou, //督查督办验收
                R.mipmap.assets_wuyefuwubiaozhun,//物业服务标准
                R.mipmap.assets_chankangongyingshang//查看供应商
        };
        Class<?>[] classArr = {RepairPlanOrderActivity.class,//修缮计划单
                RepairApprovalActivity.class,//修缮审批
                RepairFinalJudgmentActivity.class,//修缮终审
                SuperviseHandleActivity.class,//督查督办单
                SuperviseHandleAcceptanceActivity.class,//督查督办验收
                PropertyServiceStandardActivity.class,//物业服务标准
                SupplierActivity.class//查看供应商
        };

        //将没有的权限过滤掉
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

        //根据权限拼页面展示的数据
        final List<AssetsOperationBean> list = new ArrayList<>();
        for (int i = 0; i < filterList.size(); i++) {
            AssetsOperationBean bean = new AssetsOperationBean();

            int assetsIndex = 0;//资源位置下标

            PermissionCode code = PermissionCode.getById(filterList.get(i));
            switch (code) {
                //修缮计划单
                case REPAIR_PLAN:
                    assetsIndex = 0;
                    break;
                //修缮审批 zy
                case REPAIR_Approval:
                    assetsIndex = 1;
                    break;
                //修缮终审
                case REPAIR_FINAL_JUDGMENT:
                    assetsIndex = 2;
                    break;
                //督查督办单
                case SUPERVISE_HANDLE:
                    assetsIndex = 3;
                    break;
                //督查督办验收
                case SUPERVISE_CHECK_ACCEPT:
                    assetsIndex = 4;
                    break;
                //物业服务标准
                case PROPERTY_SERVISE_STANDARDS:
                    assetsIndex = 5;
                    break;
                //查看供应商
                case SEE_SUPPLIER:
                    assetsIndex = 6;
                    break;
            }
            bean.setName(nameArr[assetsIndex]);
            bean.setIcon(iconArr[assetsIndex]);
            bean.setZclass(classArr[assetsIndex]);
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
                Intent intent = new Intent(AssetsOperationMainActivity.this, bean.getZclass());
                intent.putExtra("intentType", 1);
                startActivity(intent);
            }
        });
    }

}
