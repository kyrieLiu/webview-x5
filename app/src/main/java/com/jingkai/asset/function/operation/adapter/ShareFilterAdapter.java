package com.jingkai.asset.function.operation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.operation.entity.ShareFilterBean;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/5/27 15:09
 * Description:
 */
public class ShareFilterAdapter<T extends ShareFilterBean> extends BaseRecyclerViewAdapter<T> {


    public ShareFilterAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_share_file_filter, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<T> {
        @Bind(R.id.tv_item_share_filter_menu)
        TextView mTvMenu;
        @Bind(R.id.view_share_filter_line)
        View viewLine;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(T bean, int position) {
            mTvMenu.setText(bean.getField());

            if (bean.isCheck()) {
                mTvMenu.setTextColor(context.getResources().getColor(R.color.main_color));
            } else {
                mTvMenu.setTextColor(context.getResources().getColor(R.color.main_text_color));
            }

            if (position == list.size()-1) {
                viewLine.setVisibility(View.GONE);
            } else {
                viewLine.setVisibility(View.VISIBLE);
            }
        }
    }
}
