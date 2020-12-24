package com.jingkai.asset.function.main.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseFragment;
import com.jingkai.asset.common.helper.CodePermissionHelper;
import com.jingkai.asset.common.helper.PermissionCode;
import com.jingkai.asset.config.RxConstant;
import com.jingkai.asset.function.login.activity.LoginActivity;
import com.jingkai.asset.function.main.activity.PersonalDataActivity;
import com.jingkai.asset.function.operation.activity.MySharedFilesActivity;
import com.jingkai.asset.function.waitdone.WaitDoneListActivity;
import com.jingkai.asset.manager.RxManager;
import com.jingkai.asset.utils.SpUtil;
import com.jingkai.asset.utils.view.GlideUtils;
import com.jingkai.asset.widget.dialog.SelectMenuDialog;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by liuyin on 2019/2/28 18:19
 *
 * @Describe 个人中心
 */
public class MineFragment extends BaseFragment {

    @Bind(R.id.iv_mine_head_picture)
    ImageView mIvHeadPicture;
    @Bind(R.id.tv_mine_name)
    TextView mTvName;
    @Bind(R.id.tv_mine_job)
    TextView mTvJob;
    //我的待办事项父布局
    @Bind(R.id.ll_mine_wait_done_parent)
    LinearLayout mLlWaitDoneParent;
    //我的共享文件父布局
    @Bind(R.id.ll_mine_shared_file_parent)
    LinearLayout mLlSharedFileParent;
    //版本号
    @Bind(R.id.tv_mine_version)
    TextView mTvVersion;

    private RxManager rxManager;

    @Override
    protected int getContentId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(Bundle bundle) {
        GlideUtils.loadImage(getActivity(), mIvHeadPicture, SpUtil.getHeadImage(), R.mipmap.default_head_image);
        mTvJob.setText(SpUtil.getJob());
        mTvName.setText(SpUtil.getName());
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String versionName = info.versionName;
            mTvVersion.setText("V" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    @Override
    protected void loadData() {
        rxManager = new RxManager();
        rxManager.on(RxConstant.RX_PERSON_DATA_UPDATE, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                //头像发生变化
                if (integer == 1) {
                    GlideUtils.loadImage(getActivity(), mIvHeadPicture, SpUtil.getHeadImage(), R.mipmap.default_head_image);
                } else {//个人姓名发生变化
                    mTvName.setText(SpUtil.getName());
                }
            }
        });
        //如果有共享文件权限,展示共享文件入口
        if (CodePermissionHelper.havePermission(PermissionCode.MINE_FILE_SHARE.getCode())) {
            mLlSharedFileParent.setVisibility(View.VISIBLE);
        }

        //如果有我的待办事项权限,展示待办事项入口
        if (CodePermissionHelper.havePermission(PermissionCode.MINE_WAIT_DONE.getCode())) {
            mLlWaitDoneParent.setVisibility(View.VISIBLE);
        }

    }


    @OnClick({R.id.ll_mine_personal_data, R.id.ll_mine_wait_done_parent, R.id.ll_mine_shared_file_parent, R.id.ll_mine_exit_parent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_mine_wait_done_parent:
                //我的待办事项
                Intent intent = new Intent(getActivity(), WaitDoneListActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_mine_shared_file_parent:
                //我的共享文件
                startActivity(new Intent(getActivity(), MySharedFilesActivity.class));
                break;
            //查看个人资料
            case R.id.ll_mine_personal_data:
                startActivity(new Intent(getActivity(), PersonalDataActivity.class));
                break;
            //退出
            case R.id.ll_mine_exit_parent:
                showLogoutDialog();
                break;
        }
    }

    //退出登录弹框
    private void showLogoutDialog() {
        final SelectMenuDialog dialog = new SelectMenuDialog(getActivity());
        dialog.setListener(new SelectMenuDialog.OnMenuSelectListener() {
            @Override
            public void callback() {
                dialog.dismiss();
                LoginActivity.start(getActivity());
            }
        });
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rxManager.clear();
        rxManager = null;
    }
}
