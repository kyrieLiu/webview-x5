package com.jingkai.asset.function.repair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.repair.entity.RepairDetailBean;
import com.jingkai.asset.utils.view.ViewUtil;

import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/9 10:41
 * Description:审核过程列表
 */
public class RepairAuditProcessAdapter<T extends RepairDetailBean.ProcessBean> extends BaseRecyclerViewAdapter<T> {

    public RepairAuditProcessAdapter(List<T> list, Context context) {
        super(list, context);
    }


    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_audit_process, parent, false);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends BaseViewHolder<T> {
        @Bind(R.id.view_repair_audit_process_point)
        View mViewPoint;
        @Bind(R.id.view_repair_audit_process_line_bottom)
        View mViewLineBottom;
        @Bind(R.id.view_repair_audit_process_line_top)
        View mViewLineTop;
        @Bind(R.id.tv_item_audit_process_content)
        TextView mTvContent;
        @Bind(R.id.tv_item_audit_process_time)
        TextView mTvTime;
        @Bind(R.id.tv_item_audit_process_status)
        TextView mTvStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(T bean, int position) {
            if (position == list.size() - 1) {
                mViewLineBottom.setVisibility(View.GONE);
            }
            if (position == 0) {
                mViewLineTop.setVisibility(View.GONE);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mViewPoint.getLayoutParams();
                layoutParams.topMargin = ViewUtil.dp2px(5);
                mViewPoint.setLayoutParams(layoutParams);
            }
            mTvContent.setText(bean.getEditor());
            mTvTime.setText(bean.getGmtModifiedTime());

            mTvStatus.setText("[" + bean.getStatusType() + "]");

//            String repairStatus = bean.getRepairStatus();
//            if (!TextUtils.isEmpty(repairStatus)) {
//                int status = Integer.parseInt(repairStatus);
//                setRepairStatus(status);
//            } else {
//                setRepairStatus(0);
//            }


        }

        /**
         * 展示状态
         *
         * @param status
         */
        private void setRepairStatus(int status) {
            String statusType = "";
            switch (status) {
                case 10:
                    statusType = "新建保存";
                    break;
                case 20:
                    statusType = "新建提交";
                    break;
                case 30:
                    statusType = "报审保存";
                    break;
                case 40:
                    statusType = "报审提交";
                    break;
                case 50:
                    statusType = "验收通过";
                    break;
                case 60:
                    statusType = "验收不通过";
                    break;
                case 70:
                    statusType = "终审通过";
                    break;
                case 80:
                    statusType = "终审不通过";
                    break;
                default:
                    statusType = "新建草稿";
                    break;
            }
            mTvStatus.setText("[" + statusType + "]");
        }
    }
}
