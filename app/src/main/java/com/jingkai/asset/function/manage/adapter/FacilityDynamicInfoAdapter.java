package com.jingkai.asset.function.manage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.manage.entity.EquipmentDetailBean;

import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/12 17:31
 * Description:
 */
public class FacilityDynamicInfoAdapter<T extends EquipmentDetailBean.RepairListBean> extends BaseRecyclerViewAdapter<T> {


    public FacilityDynamicInfoAdapter(List<T> list, Context context) {
        super(list, context);
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_facility_dynamic_info, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<T> {
        @Bind(R.id.tv_item_device_dynamic_time)
        TextView mTvTime;
        @Bind(R.id.tv_item_device_dynamic_name)
        TextView mTvName;
        @Bind(R.id.tv_item_device_dynamic_type)
        TextView mTvType;
        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(T bean, int position) {
            mTvTime.setText(bean.getGmtCreateDateStr());
            mTvName.setText(bean.getCreator());
            mTvType.setText(bean.getRepairTypeName());
        }
    }
}
