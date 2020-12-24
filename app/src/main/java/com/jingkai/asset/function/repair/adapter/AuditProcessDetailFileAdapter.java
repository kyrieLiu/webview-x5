package com.jingkai.asset.function.repair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.repair.entity.FileBean;

import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/10 10:24
 * Description:审核过程详情附件列表
 */
public class AuditProcessDetailFileAdapter<T extends FileBean> extends BaseRecyclerViewAdapter<T> {

    public AuditProcessDetailFileAdapter(List<T> list, Context context) {
        super(list, context);
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_repair_audit_process_file, parent, false);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends BaseViewHolder<T> {
        @Bind(R.id.tv_item_audit_process_file_name)
        TextView mTvFileName;

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(T bean, int position) {
            mTvFileName.setText(bean.getName());
        }
    }
}
