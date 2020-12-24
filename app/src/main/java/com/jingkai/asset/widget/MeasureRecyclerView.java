package com.jingkai.asset.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by liuyin on 2019/2/27 16:47
 * Description:解决scrollView嵌套RecyclerView,导致RecyclerView条目显示不全问题
 */
public class MeasureRecyclerView extends RecyclerView {
    public MeasureRecyclerView(Context context) {
        super(context);
    }

    public MeasureRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //限制RecyclerView自身的滑动，页面滑动仅依靠ScrollView实现
        setHasFixedSize(true);
        setNestedScrollingEnabled(false);
    }

    public MeasureRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
