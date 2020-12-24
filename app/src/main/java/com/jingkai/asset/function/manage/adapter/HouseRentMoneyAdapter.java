package com.jingkai.asset.function.manage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.manage.entity.HouseRentContractBean;
import com.jingkai.asset.utils.view.ViewUtil;

import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/15 11:34
 * Description:出租详情计费
 */
public class HouseRentMoneyAdapter<T extends HouseRentContractBean.ConBModeListBean> extends BaseRecyclerViewAdapter<T> {


    public HouseRentMoneyAdapter(List<T> list, Context context) {
        super(list, context);
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_house_rent_money, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<T> {
        //计费阶段
        @Bind(R.id.tv_item_rent_status)
        TextView mTvStatus;
        //计费方式
        @Bind(R.id.tv_item_rent_type)
        TextView mTvType;
        //租金单价
        @Bind(R.id.tv_item_rent_price)
        TextView mTvPrice;
        @Bind(R.id.tv_item_rent_index)
        TextView mTvIndex;


        private ViewUtil viewUtil = ViewUtil.getViewUtil();

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(T bean, int position) {
            mTvIndex.setText(position+1+".");
            viewUtil.setTextMessage(mTvStatus, "计费阶段", bean.getStartDate() + " — " + bean.getEndDate());
            viewUtil.setTextMessage(mTvType, "计费方式", bean.getBillingModeTitle());
            viewUtil.setTextMessage(mTvPrice, "租金单价", bean.getRent());
        }
    }
}
