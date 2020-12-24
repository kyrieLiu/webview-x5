package com.jingkai.asset.function.operation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.operation.entity.AssetsOperationBean;
import com.jingkai.asset.utils.PixelUtil;

import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/15 16:25
 * Description:
 */
public class AssetsOperationMainAdapter<T extends AssetsOperationBean> extends BaseRecyclerViewAdapter<T> {


    public AssetsOperationMainAdapter(List<T> list, Context context) {
        super(list, context);
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_assets_operate_main, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<T> {
        @Bind(R.id.iv_item_assets_operate_icon)
        ImageView mIvIcon;
        @Bind(R.id.tv_item_assets_operate_name)
        TextView mTvName;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(T bean, int position) {
            mIvIcon.setImageResource(bean.getIcon());
            mTvName.setText(bean.getName());
            if (position == getList().size() - 1){
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0,0,0, PixelUtil.dip2px(context,40));
                mTvName.setLayoutParams(layoutParams);
            }
        }
    }
}
