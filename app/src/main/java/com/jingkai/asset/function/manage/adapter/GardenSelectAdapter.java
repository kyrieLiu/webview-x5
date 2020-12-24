package com.jingkai.asset.function.manage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.manage.entity.GardenInfoBean;

import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/12 11:35
 * Description:园区切换选择列表
 */
public class GardenSelectAdapter<T extends GardenInfoBean.ItemsBean> extends BaseRecyclerViewAdapter<T> {


    public GardenSelectAdapter(List<T> list, Context context) {
        super(list, context);
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_garden_select, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<T> {
        @Bind(R.id.tv_item_select_garden_name)
        TextView mTvName;
        @Bind(R.id.iv_item_select_garden_check)
        ImageView mIvCheck;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(T bean, int position) {
            mTvName.setText(bean.getName());
            if (bean.getIsChecked()==0){
                mIvCheck.setImageResource(R.mipmap.unselect_icon);
            }else{
                mIvCheck.setImageResource(R.mipmap.select_icon);
            }
        }
    }
}
