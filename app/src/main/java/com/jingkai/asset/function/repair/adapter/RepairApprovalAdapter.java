package com.jingkai.asset.function.repair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.operation.entity.RepairApprovalBean;
import com.jingkai.asset.utils.TimeUtils;
import com.jingkai.asset.utils.view.ViewUtil;

import java.text.SimpleDateFormat;

import butterknife.Bind;

/**
 * Created by zy on 2019/8/28
 * Description: 修缮审批列表适配
 */
public class RepairApprovalAdapter<T extends RepairApprovalBean.ItemsBean> extends BaseRecyclerViewAdapter<T> {


    public RepairApprovalAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_repair_approval, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<T> {

        //未读红点标记
        @Bind(R.id.view_item_repair_approval_warn)
        View redCircle;
        //计划名称
        @Bind(R.id.tv_item_repair_approval_title)
        TextView mTvTitle;
        //所属项目
        @Bind(R.id.tv_item_repair_approval_belongProject)
        TextView mTvBelongProject;
        //经营单位
        @Bind(R.id.tv_item_repair_approval_operationUnit)
        TextView mTvOperationUnit;
        //计划时间
        @Bind(R.id.tv_item_repair_approval_planTime)
        TextView mTvPlanTime;
        //状态
        @Bind(R.id.tv_item_repair_approval_status)
        TextView mTvStatus;

        private ViewUtil util;

        public ViewHolder(View itemView) {
            super(itemView);
            util = ViewUtil.getViewUtil();
        }

        @Override
        protected void bind(T bean, int position) {
            mTvTitle.setText(bean.getName());
            mTvStatus.setText(bean.getRepairPlanStatus());
            util.setTextMessage(mTvBelongProject, "所属项目", bean.getIdAcParkTitle());
            util.setTextMessage(mTvOperationUnit, "经营单位", bean.getNameRbacDepartment());
            util.setTextMessage(mTvPlanTime, "计划时间", TimeUtils.changeTimeFormat(new SimpleDateFormat("yyyy-MM-dd"),
                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"), bean.getGmtStart()));

            if(bean.getIsRead() == 0 ){
                redCircle.setVisibility(View.VISIBLE);
            }else {
                redCircle.setVisibility(View.GONE);
            }
        }
    }
}
