package com.jingkai.asset.function.repair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.operation.entity.RepairPlanOrderBean;
import com.jingkai.asset.utils.TimeUtils;
import com.jingkai.asset.utils.view.ViewUtil;

import java.text.SimpleDateFormat;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/16 10:15
 * Description:
 */
public class RepairPlanOrderAdapter<T extends RepairPlanOrderBean.ItemsBean> extends BaseRecyclerViewAdapter<T> {


    public RepairPlanOrderAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_repair_plan_order, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<T> {
        //计划名称
        @Bind(R.id.tv_item_repair_plan_title)
        TextView mTvTitle;
        //所属项目
        @Bind(R.id.tv_item_repair_plan_belongProject)
        TextView mTvBelongProject;
        //经营单位
        @Bind(R.id.tv_item_repair_plan_operationUnit)
        TextView mTvOperationUnit;
        //计划时间
        @Bind(R.id.tv_item_repair_plan_planTime)
        TextView mTvPlanTime;
        //状态
        @Bind(R.id.tv_item_repair_plan_status)
        TextView mTvStatus;

        private ViewUtil util;

        public ViewHolder(View itemView) {
            super(itemView);
            util = ViewUtil.getViewUtil();
        }

        @Override
        protected void bind(T bean, int position) {
            mTvTitle.setText(bean.getName());
            mTvStatus.setText(bean.getRepairStatusTitle());
            util.setTextMessage(mTvBelongProject, "所属项目", bean.getIdAcParkTitle());
            util.setTextMessage(mTvOperationUnit, "经营单位", bean.getNameRbacDepartment());
            util.setTextMessage(mTvPlanTime, "计划时间", TimeUtils.changeTimeFormat(new SimpleDateFormat("yyyy-MM-dd"),
                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"), bean.getGmtStart()));
        }
    }
}
