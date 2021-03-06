package com.jingkai.asset.function.operation.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.operation.entity.SharedFilesBean;
import com.jingkai.asset.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;

/**
 * 共享文件
 */
public class MySharedFilesAdapter<T extends SharedFilesBean.ItemsBean> extends BaseRecyclerViewAdapter<T> {



    public MySharedFilesAdapter(List<T> list, Context context) {
        super(list, context);
    }

    public MySharedFilesAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shared_files, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseViewHolder<T> {
        @Bind(R.id.tv_shared_file_name)
        TextView mTvSharedFileName;
        @Bind(R.id.tv_shared_file_time)
        TextView mTvSharedFileTime;
        @Bind(R.id.tv_shared_file_type)
        TextView mTvType;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(T bean, int position) {
            mTvSharedFileName.setText(bean.getFileSharingTypeTitle());
            String time=TimeUtils.changeTimeFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),bean.getGmtCreate());
            mTvSharedFileTime.setText(time);
            String url=bean.getFileAddr();
            if (!TextUtils.isEmpty(url)){
                int dotIndex = url.lastIndexOf(".");
                /* 获取文件的后缀名 */
                String end = url.substring(dotIndex).toLowerCase();
                mTvType.setText(end);
            }
        }


    }
}
