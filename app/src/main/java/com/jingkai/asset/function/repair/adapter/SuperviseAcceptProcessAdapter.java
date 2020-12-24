package com.jingkai.asset.function.repair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.supervise.entity.SuperviseDetailBean;
import com.jingkai.asset.utils.view.ViewUtil;

import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/9 10:41
 * Description:督查督办验收过程列表
 */
public class SuperviseAcceptProcessAdapter<T extends SuperviseDetailBean.ActionBean> extends BaseRecyclerViewAdapter<T> {

    public SuperviseAcceptProcessAdapter(List<T> list, Context context) {
        super(list, context);
    }


    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_audit_process, parent, false);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends BaseViewHolder<T> {
        @Bind(R.id.view_repair_audit_process_point)
        View mViewPoint;
        @Bind(R.id.view_repair_audit_process_line_bottom)
        View mViewLineBottom;
        @Bind(R.id.view_repair_audit_process_line_top)
        View mViewLineTop;
        @Bind(R.id.tv_item_audit_process_content)
        TextView mTvContent;
        @Bind(R.id.tv_item_audit_process_status)
        TextView mTvStatus;
        @Bind(R.id.tv_item_audit_process_time)
        TextView mTvTime;
        public MyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(T bean, int position) {
            if (position==list.size()-1){
                mViewLineBottom.setVisibility(View.GONE);
            }
            if (position==0){
                mViewLineTop.setVisibility(View.GONE);
                LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) mViewPoint.getLayoutParams();
                layoutParams.topMargin= ViewUtil.dp2px(5);
                mViewPoint.setLayoutParams(layoutParams);
            }
            mTvContent.setText(bean.getCreator());
            mTvStatus.setText( "["+bean.getStatusChange()+"]");
            mTvTime.setText(bean.getGmtCreate());
        }
    }
}
