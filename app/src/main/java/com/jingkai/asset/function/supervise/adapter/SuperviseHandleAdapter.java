package com.jingkai.asset.function.supervise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.supervise.entity.SuperviseHandleBean;
import com.jingkai.asset.utils.view.ViewUtil;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/16 10:15
 * Description:督查督办列表
 */
public class SuperviseHandleAdapter<T extends SuperviseHandleBean.ItemsBean> extends BaseRecyclerViewAdapter<T> {


    public SuperviseHandleAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_supervise_handle, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<T> {
        @Bind(R.id.tv_item_supervise_handle_title)
        TextView mTvTitle;
        @Bind(R.id.tv_item_supervise_handle_belongProject)
        TextView mTvBelongProject;
        @Bind(R.id.tv_item_supervise_handle_operationUnit)
        TextView mTvOperationUnit;
        @Bind(R.id.tv_item_supervise_charge_person)
        TextView mTvChargePerson;
        @Bind(R.id.tv_item_supervise_handle_status)
        TextView mTvStatus;
        private ViewUtil util;
        public ViewHolder(View itemView) {
            super(itemView);
            util=ViewUtil.getViewUtil();
        }

        @Override
        protected void bind(T bean, int position) {
            mTvTitle.setText(bean.getMissionName());
            util.setTextMessage(mTvBelongProject,"所属项目",bean.getNameAcPark());
            util.setTextMessage(mTvOperationUnit,"经营单位",bean.getNameRbacDepartment());
            util.setTextMessage(mTvChargePerson,"责任人",bean.getChargeInfo());
            mTvStatus.setText(bean.getSupervisoryStatusTitle());
        }
    }
}
