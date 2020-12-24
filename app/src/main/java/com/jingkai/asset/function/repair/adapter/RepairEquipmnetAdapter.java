package com.jingkai.asset.function.repair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.repair.entity.RepairEquipmentBean;

import java.util.List;

import butterknife.Bind;

/**
 * Created by zhangyang
 * Date: 2019/8/29
 * Description: 修缮单详情 资产列表适配
 */
public class RepairEquipmnetAdapter<T extends RepairEquipmentBean> extends BaseRecyclerViewAdapter<T> {




    public RepairEquipmnetAdapter(List<T> list, Context context) {
        super(list, context);
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_repair_equipment, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<T> {
        //序号
        @Bind(R.id.tv_repair_assets_index)
        TextView mTvRepairAssetsIndex;
        //设备名称
        @Bind(R.id.tv_repair_assets_name)
        TextView mTvRepairAssetsName;
        //设备分类
        @Bind(R.id.tv_repair_assets_classify)
        TextView mTvRepairAssetsClassify;
        //设备位置
        @Bind(R.id.tv_repair_location)
        TextView mTvRepairLocation;
        //分割线
        @Bind(R.id.repair_equipment_item_line)
        View mRepairEquipmentItemLine;


        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(T bean, int position) {

            if (getList().size() > 1) {
                String index = position+1 < 10 ? "0" + (position + 1) : position+1 + "";
                mTvRepairAssetsIndex.setText("序号: " + index);
                mTvRepairAssetsIndex.setVisibility(View.VISIBLE);
            } else {
                mTvRepairAssetsIndex.setVisibility(View.GONE);
            }

            mTvRepairAssetsName.setText("设备名称: " + bean.getNameAcEquipment());
            mTvRepairLocation.setText("资产位置: " + bean.getNameAcAssetPosition());
            mTvRepairAssetsClassify.setText("资产分类: " + bean.getNameAcAssetCategory());

            if(position == getList().size() - 1){
                mRepairEquipmentItemLine.setVisibility(View.GONE);
            }else {
                mRepairEquipmentItemLine.setVisibility(View.VISIBLE);
            }
        }
    }
}
