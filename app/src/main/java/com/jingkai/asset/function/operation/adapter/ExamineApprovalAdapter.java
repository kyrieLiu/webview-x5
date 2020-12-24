package com.jingkai.asset.function.operation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.operation.entity.FinalExamineBean;
import com.jingkai.asset.utils.TimeUtils;
import com.jingkai.asset.utils.view.ViewUtil;

import java.text.SimpleDateFormat;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/16 13:49
 * Description:审批列表适配器
 */
public class ExamineApprovalAdapter<T extends FinalExamineBean.ItemsBean> extends BaseRecyclerViewAdapter<T> {

    private int pageIndex;//2已审批   1待审批

    public ExamineApprovalAdapter(Context context, int pageIndex) {
        super(context);
        this.pageIndex = pageIndex;
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_examine_approval, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<T> {
        //红点提醒
        @Bind(R.id.view_item_examine_approval_warn)
        View mViewWarn;
        //终审名称
        @Bind(R.id.tv_item_examine_approval_title)
        TextView mTvTitle;
        //所属项目
        @Bind(R.id.tv_item_examine_approval_belongProject)
        TextView mTvBelongProject;
        //经营单位
        @Bind(R.id.tv_item_examine_approval_operationUnit)
        TextView mTvOperationUnit;
        //计划时间
        @Bind(R.id.tv_item_examine_approval_planTime)
        TextView mTvPlanTime;
        //信息内容父布局
        @Bind(R.id.ll_item_examine_approval_title_parent)
        LinearLayout mLlParent;
        @Bind(R.id.tv_item_examine_approval_status)
        TextView mTvStatus;

        private ViewUtil util;

        public ViewHolder(View itemView) {
            super(itemView);
            util = ViewUtil.getViewUtil();
        }

        @Override
        protected void bind(T bean, int position) {
            mTvTitle.setText(bean.getName());
            util.setTextMessage(mTvBelongProject, "所属项目", bean.getIdAcParkTitle());
            util.setTextMessage(mTvOperationUnit, "经营单位", bean.getNameRbacDepartment());
            util.setTextMessage(mTvPlanTime, "计划时间", TimeUtils.changeTimeFormat(new SimpleDateFormat("yyyy-MM-dd"),
                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"), bean.getGmtStart()));
            if (pageIndex == 2) {
                mTvStatus.setText(bean.getFinalReviewFailedStatus());
            }
            //已审批不显示红点,待审批未读消息展示红点
            if (pageIndex == 1 && bean.getIsRead() != 1) {
                mViewWarn.setVisibility(View.VISIBLE);
            } else {
                mViewWarn.setVisibility(View.GONE);
            }

        }
    }
}
