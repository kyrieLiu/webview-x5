package com.jingkai.asset.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jingkai.asset.R;
import com.jingkai.asset.function.manage.entity.GardenInfoBean;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuyin on 2019/4/12 11:02
 * Description:图片选择弹框
 */
public class SelectPictureDialog extends Dialog {

    private GardenInfoBean.ItemsBean selectBean;

    public SelectPictureDialog(@NonNull Context context) {
        super(context, R.style.ThemeCustomDialog);
        setContentView(R.layout.dialog_select_picture);

        ButterKnife.bind(this);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }


    @OnClick({R.id.tv_dialog_select_photo_graph, R.id.tv_dialog_select_album, R.id.tv_dialog_select_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_dialog_select_photo_graph:
                dismiss();
                if (listener != null) listener.selectCallback(1);
                break;
            case R.id.tv_dialog_select_album:
                dismiss();
                if (listener != null) listener.selectCallback(2);
                break;
            case R.id.tv_dialog_select_cancel:
                dismiss();
                break;
        }
    }

    public interface SelectPictureListener {
        void selectCallback(int type);
    }

    private SelectPictureListener listener;

    public void setListener(SelectPictureListener listener) {
        this.listener = listener;
    }
}
