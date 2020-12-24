package com.jingkai.asset.function.supervise.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.operation.entity.SuperviseHandleAcceptanceBean;
import com.jingkai.asset.utils.view.ViewUtil;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/16 14:42
 * Description:
 */
public class SuperviseHandleAcceptanceAdapter<T extends SuperviseHandleAcceptanceBean.ItemsBean> extends BaseRecyclerViewAdapter<T> {

    private int index;//1 已验收    2待验收

    public SuperviseHandleAcceptanceAdapter(Context context, int index) {
        super(context);
        this.index = index;
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_supervise_handle_acceptance, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<T> {
        @Bind(R.id.view_item_supervise_handle_warn)
        View mViewWarn;
        @Bind(R.id.tv_item_supervise_handle_title)
        TextView mTvItemTitle;
        @Bind(R.id.tv_item_supervise_handle_belongProject)
        TextView mTvItemBelongProject;
        @Bind(R.id.tv_item_supervise_handle_operationUnit)
        TextView mTvItemOperationUnit;
        @Bind(R.id.tv_item_supervise_handle_planTime)
        TextView mTvItemPlanTime;
        @Bind(R.id.ll_item_supervise_handle_title_parent)
        LinearLayout mLlItemTitleParent;
        @Bind(R.id.tv_item_supervise_handle_status)
        TextView mTvStatus;

        private ViewUtil util;

        public ViewHolder(View itemView) {
            super(itemView);
            util = ViewUtil.getViewUtil();
        }

        @Override
        protected void bind(T bean, int position) {
            mTvItemTitle.setText(bean.getMissionName());
            util.setTextMessage(mTvItemBelongProject, "所属项目", bean.getNameAcPark());
            util.setTextMessage(mTvItemOperationUnit, "经营单位", bean.getNameRbacDepartment());
            util.setTextMessage(mTvItemPlanTime, "责任人", bean.getChargeInfo());
            if (TextUtils.equals("0", bean.getIsRead()) && index == 2) {//是否阅读 0：未读；1：已读
                mViewWarn.setVisibility(View.VISIBLE);
            } else {
                mViewWarn.setVisibility(View.GONE);
            }
            //已验收展示验收状态,待验收不展示
            if (index == 1) {
                mTvStatus.setText(bean.getSupervisoryStatusTitle());
                mTvStatus.setVisibility(View.VISIBLE);
            } else {
                mTvStatus.setVisibility(View.GONE);
            }
        }
    }
}
