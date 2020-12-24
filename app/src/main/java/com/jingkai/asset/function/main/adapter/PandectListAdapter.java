package com.jingkai.asset.function.main.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.function.main.entity.PandectBean;

import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/2/21 14:00
 * 总览适配器
 */
public class PandectListAdapter<T extends PandectBean> extends BaseRecyclerViewAdapter<T> {

    public PandectListAdapter(List<T> list, Context context) {
        super(list, context);
    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pandect, parent, false);
        return new ViewHolder(view);

    }


    class ViewHolder extends BaseViewHolder<T> {

        @Bind(R.id.iv_item_pandect_image)
        ImageView mIvImage;
        @Bind(R.id.iv_item_pandect_name)
        TextView mIvName;
        @Bind(R.id.iv_item_pandect_introduce)
        TextView mIvIntroduce;

        public ViewHolder(View itemView) {
            super(itemView);
        }


        @Override
        protected void bind(T bean, int position) {
            mIvImage.setImageResource(bean.getImage());
            mIvIntroduce.setText(bean.getData());
            if (position == 0){
                if (bean.getTitle() == null)
                    return;
                SpannableString spannableString = new SpannableString(bean.getTitle());
                SuperscriptSpan superscriptSpan = new SuperscriptSpan(); //上标
                spannableString.setSpan(superscriptSpan, 6, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(0.5f);
                spannableString.setSpan(relativeSizeSpan, 6, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mIvName.setText(spannableString);
            }else {
                mIvName.setText(bean.getTitle());
            }

        }
    }
}


