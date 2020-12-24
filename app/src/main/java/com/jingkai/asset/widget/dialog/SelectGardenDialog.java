package com.jingkai.asset.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.jingkai.asset.R;
import com.jingkai.asset.callback.OnItemClickRecyclerListener;
import com.jingkai.asset.function.manage.adapter.GardenSelectAdapter;
import com.jingkai.asset.function.manage.entity.GardenInfoBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuyin on 2019/4/12 11:02
 * Description:切换园区弹窗
 */
public class SelectGardenDialog extends Dialog {
    @Bind(R.id.rv_dialog_select_garden)
    RecyclerView mRvSelectGarden;

    private GardenInfoBean.ItemsBean selectBean;

    public SelectGardenDialog(@NonNull Context context) {
        super(context, R.style.ThemeCustomDialog);
        setContentView(R.layout.dialog_garden_select);

        ButterKnife.bind(this);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
    }

    public void setData(final List<GardenInfoBean.ItemsBean> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRvSelectGarden.setLayoutManager(layoutManager);
        final GardenSelectAdapter adapter = new GardenSelectAdapter(list, getContext());
        mRvSelectGarden.setAdapter(adapter);
        adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
            @Override
            public void onItemClick(View view, int position) {
                for (int i = 0; i < list.size(); i++) {
                    GardenInfoBean.ItemsBean bean = list.get(i);
                    if (i == position) {
                        bean.setIsChecked(1);
                        selectBean=bean;
                    } else {
                        bean.setIsChecked(0);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    @OnClick({R.id.iv_dialog_select_garden_close, R.id.bt_select_garden_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_dialog_select_garden_close:
                dismiss();
                break;
            case R.id.bt_select_garden_confirm:
                if (listener!=null)listener.selectCallback(selectBean);
                break;
        }
    }
    public interface GardenSelectListener{
        void selectCallback(GardenInfoBean.ItemsBean bean);
    }

    private GardenSelectListener listener;

    public void setListener(GardenSelectListener listener) {
        this.listener = listener;
    }
}
