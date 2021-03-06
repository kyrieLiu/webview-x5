package com.jingkai.asset.function.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.main.activity.MainActivity;
import com.jingkai.asset.function.main.entity.WaitDoneBean;

import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/2/27 15:19
 * Description:待办事项
 */
public class HomeWaitDoneAdapter<T extends WaitDoneBean.ItemsBean> extends BaseRecyclerViewAdapter<T> {

    public HomeWaitDoneAdapter(List<T> list, Context context) {
        super(list, context);
    }

    public HomeWaitDoneAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_wait_done, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<T> {
        @Bind(R.id.tv_item_wait_done_status)
        TextView mTvStatus;
        @Bind(R.id.rl_item_wait_done_root)
        RelativeLayout mRlRoot;
        @Bind(R.id.tv_item_wait_done_type)
        TextView mTvType;
        //事项名称
        @Bind(R.id.tv_item_wait_done_name)
        TextView mTvName;
        //时间
        @Bind(R.id.tv_item_wait_done_time)
        TextView mTvTime;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(T bean, int position) {


            String status = bean.getTypeStatus();
            if (!TextUtils.isEmpty(status)) {
                switch (status) {
                    case "待预审":
                        mTvStatus.setTextColor(Color.parseColor("#13AE85"));
                        break;
                    case "待反馈":
                        mTvStatus.setTextColor(Color.parseColor("#0AC87A"));
                        break;
                    case "待验收":
                        mTvStatus.setTextColor(Color.parseColor("#53db4f"));
                        break;
                    case "储备中":
                        mTvStatus.setTextColor(Color.parseColor("#28DEC9"));
                        break;
                    case "待报审":
                        mTvStatus.setTextColor(Color.parseColor("#28DEC9"));
                        break;
                    case "待终审":
                        mTvStatus.setTextColor(Color.parseColor("#51A8FF"));
                        break;

                }
            }
            mTvStatus.setText(status);

            mTvName.setText(bean.getTaskName());
            mTvType.setText(bean.getTypeName());

            mTvTime.setText(bean.getGmtCreate());

            if (context instanceof MainActivity) {
                mRlRoot.setBackground(null);
            }
        }


    }
}
