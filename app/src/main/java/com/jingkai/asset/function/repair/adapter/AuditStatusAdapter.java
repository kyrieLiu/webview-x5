package com.jingkai.asset.function.repair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.repair.entity.AuditStatusBean;
import com.jingkai.asset.utils.view.ViewUtil;

import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/8 19:49
 * Description:横向流程图适配器
 */
public class AuditStatusAdapter<T extends AuditStatusBean> extends BaseRecyclerViewAdapter<T> {

    private int parentWidth;

    public AuditStatusAdapter(List<T> list, Context context, int parentWidth) {
        super(list, context);
        this.parentWidth = parentWidth;
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_repair_audit_status, parent, false);

        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<T> {
        @Bind(R.id.view_repair_detail_point)
        View mViewPoint;
        @Bind(R.id.view_repair_process_right)
        View mViewRight;
        @Bind(R.id.view_repair_process_left)
        View mViewLeft;
        @Bind(R.id.tv_repair_process_status)
        TextView mTvStatus;
        @Bind(R.id.ll_item_repair_parocess_root)
        LinearLayout mLlRoot;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(T bean, int position) {

            int itemWidth = parentWidth / list.size();
            ViewGroup.LayoutParams layoutParams = mLlRoot.getLayoutParams();
            layoutParams.width = itemWidth;
            mLlRoot.setLayoutParams(layoutParams);

            if (position == 0) {//第一条不显示左连接线
                mViewLeft.setVisibility(View.GONE);
            } else if (position == list.size() - 1) {//最后一条不显示右连接线
                mViewRight.setVisibility(View.GONE);
            } else {
                mViewLeft.setVisibility(View.VISIBLE);
                mViewRight.setVisibility(View.VISIBLE);
            }

            //如果当前流程已经完成已经完成,将当前节点的左连接线置为蓝色
            if (bean.getHaveDone() == 1) {
                if (bean.isLastDone()) {
                    mViewPoint.setBackground(context.getResources().getDrawable(R.drawable.shape_circle_blue));
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mViewPoint.getLayoutParams();
                    int size = ViewUtil.getDp(context, 14);
                    params.width = size;
                    params.height = size;
                    params.topMargin = 0;
                    mViewPoint.setLayoutParams(params);

                } else {
                    mViewPoint.setBackground(context.getResources().getDrawable(R.drawable.shape_circle_french_blue));
                    //如果后面的一个流程已经完成已经完成,将当前节点的右连接线置为蓝色
                    mViewRight.setBackgroundColor(context.getResources().getColor(R.color.french_blue));
                }
                mViewLeft.setBackgroundColor(context.getResources().getColor(R.color.french_blue));
                mTvStatus.setTextColor(context.getResources().getColor(R.color.main_text_color));
            } else {
                mViewPoint.setBackground(context.getResources().getDrawable(R.drawable.shape_circle_grey));
                mTvStatus.setTextColor(context.getResources().getColor(R.color.gray_text_color));
            }
            mTvStatus.setText(bean.getStatusName());
        }
    }
}
