package com.jingkai.asset.function.manage.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.manage.entity.HouseRentContractBean;
import com.jingkai.asset.utils.view.ViewUtil;
import com.jingkai.asset.widget.MeasureRecyclerView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/15 11:34
 * Description:出租详情列表
 */
public class HouseRentAdapter<T extends HouseRentContractBean> extends BaseRecyclerViewAdapter<T> {


    public HouseRentAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_house_rent_info, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<T> {
        //合同名称
        @Bind(R.id.tv_item_rent_contract_name)
        TextView mTvName;
        //外部合同编号
        @Bind(R.id.tv_item_rent_contract_out_number)
        TextView mTvOutNumber;
        //系统合同编号
        @Bind(R.id.tv_item_rent_contract_system_number)
        TextView mTvSystemNumber;
        //承租人
        @Bind(R.id.tv_item_rent_contract_tenant)
        TextView mTvTenant;
        //所属行业
        @Bind(R.id.tv_item_rent_contract_industry_involved)
        TextView mTvIndustryInvolved;
        //合同状态
        @Bind(R.id.tv_item_rent_contract_status)
        TextView mTvStatus;
        //租赁期限
        @Bind(R.id.tv_item_rent_contract_deadline)
        TextView mTvDeadline;

        //押金方式
        @Bind(R.id.tv_item_rent_contract_deposit_way)
        TextView mTvDepositWay;
        //租金父布局
        @Bind(R.id.ll_item_rent_parent)
        LinearLayout mLlRentParent;
        @Bind(R.id.mrv_item_rent)
        MeasureRecyclerView mMrvRent;

        private ViewUtil viewUtil = ViewUtil.getViewUtil();

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(T bean, int position) {
            viewUtil.setTextMessage(mTvName, "合同名称", bean.getContractName());
            viewUtil.setTextMessage(mTvOutNumber, "外部合同编号", bean.getOutContractNo());
            viewUtil.setTextMessage(mTvSystemNumber, "系统合同编号", bean.getInContractNo());
            viewUtil.setTextMessage(mTvTenant, "承租人", bean.getCustomerName());
            viewUtil.setTextMessage(mTvIndustryInvolved, "所属行业", bean.getIndustryTypeName());
            viewUtil.setTextMessage(mTvStatus, "合同状态", bean.getContractStatusTitle());
            viewUtil.setTextMessage(mTvDeadline, "租赁期限", bean.getGmtStartDateStr() + " — " + bean.getGmtEndDateStr());
            viewUtil.setTextMessage(mTvDepositWay, "押付方式", bean.getDepositType());
            List<HouseRentContractBean.ConBModeListBean> list = bean.getConBModeList();
            if (list != null && list.size() > 0) {
                mLlRentParent.setVisibility(View.VISIBLE);
                HouseRentMoneyAdapter adapter = new HouseRentMoneyAdapter(list, context);
                LinearLayoutManager manager = new LinearLayoutManager(context);
                mMrvRent.setLayoutManager(manager);
                mMrvRent.setAdapter(adapter);
            } else {
                mLlRentParent.setVisibility(View.GONE);
            }

        }
    }
}
