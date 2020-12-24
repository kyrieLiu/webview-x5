package com.jingkai.asset.widget.popup;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.jingkai.asset.R;
import com.jingkai.asset.callback.OnItemClickRecyclerListener;
import com.jingkai.asset.function.operation.adapter.ShareFilterAdapter;
import com.jingkai.asset.function.operation.entity.ShareFilterBean;
import com.jingkai.asset.utils.view.ViewUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liuyin on 2019/5/27 14:55
 * Description:共享文件筛选弹框
 */
public class ShareFileFilterPop extends PopupWindow {

    @Bind(R.id.lv_pop_share_file_list)
    RecyclerView mLvFilter;
    private Context context;
    private ShareFilterAdapter<ShareFilterBean> adapter;

    public ShareFileFilterPop(Context context) {
        super(context);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.pop_share_file_filter, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        setOutsideTouchable(true);
        setFocusable(true);
        initView();
    }


    private void initView() {

        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mLvFilter.setLayoutManager(manager);
        adapter = new ShareFilterAdapter<>(context);
        mLvFilter.setAdapter(adapter);

        adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<ShareFilterBean> list = adapter.getList();
                ShareFilterBean selectBean = null;
                for (int i = 0; i < list.size(); i++) {
                    ShareFilterBean bean = list.get(i);
                    if (i == position) {
                        selectBean = bean;

                        bean.setCheck(true);
                    } else {
                        bean.setCheck(false);
                    }
                }
                adapter.notifyDataSetChanged();
                if (listener != null) {
                    listener.callback(selectBean);
                }

                dismiss();
            }
        });

    }

    /**
     * 填充数据
     *
     * @param list
     */
    public void setData(List<ShareFilterBean> list) {
        if (list.size() > 10) {
            setHeight(ViewUtil.dp2px(300));
        }
        adapter.setList(list);
    }

    private OnFilterSelectListener listener;

    public interface OnFilterSelectListener {
        void callback(ShareFilterBean bean);
    }

    public void setListener(OnFilterSelectListener listener) {
        this.listener = listener;
    }
}
