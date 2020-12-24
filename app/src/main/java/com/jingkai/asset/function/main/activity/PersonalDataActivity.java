package com.jingkai.asset.function.main.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.common.activity.ModifyActivity;
import com.jingkai.asset.config.AppConstants;
import com.jingkai.asset.config.RxConstant;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.main.entity.HeadImageUploadBean;
import com.jingkai.asset.manager.RxManager;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.ImageUtils;
import com.jingkai.asset.utils.PermissionHelper;
import com.jingkai.asset.utils.SpUtil;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.utils.selectpicture.ImageSelectUtils;
import com.jingkai.asset.utils.selectpicture.MultiImageSelectorActivity;
import com.jingkai.asset.utils.view.GlideUtils;
import com.jingkai.asset.widget.dialog.SelectPictureDialog;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liuyin on 2019/5/27 11:05
 * Describe: 个人资料页面
 */
public class PersonalDataActivity extends BaseActivity {
    //头像
    @Bind(R.id.iv_personal_data_head_protrait)
    ImageView mIvHeadProtrait;
    //姓名
    @Bind(R.id.tv_personal_data_name)
    TextView mTvName;
    //电话
    @Bind(R.id.tv_personal_data_phone)
    TextView mTvPhone;

    private PermissionHelper mHelper;
    //图片选择弹框
    private SelectPictureDialog pictureDialog;
    //图片选择器
    private ImageSelectUtils imageSelectUtils;
    private File mTmpFile;

    private static final int REQUEST_CAMERA = 100;//去拍照
    private static final int REQUEST_NAME = 200;//去修改姓名

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_personal_data;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("个人资料");
    }

    @Override
    protected void loadData() {
        mTvName.setText(SpUtil.getName());

        String phone = SpUtil.getPhone();
        if (!TextUtils.isEmpty(phone) && phone.length() >= 11) {
            phone = phone.substring(0, 3) + "*****" + phone.substring(8);
        }
        mTvPhone.setText(phone);
        GlideUtils.loadImage(PersonalDataActivity.this, mIvHeadProtrait, SpUtil.getHeadImage(), R.mipmap.default_head_image);

        initSelectPicture();//初始化图片选择器
    }


    @OnClick({R.id.ll_personal_data_head_parent, R.id.ll_personal_data_name_parent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //修改头像
            case R.id.ll_personal_data_head_parent:
                showPictureSelectDialog();
                break;
            //修改姓名
            case R.id.ll_personal_data_name_parent:
                Intent intent = new Intent(PersonalDataActivity.this, ModifyActivity.class);
                startActivityForResult(intent, REQUEST_NAME);
                break;
        }
    }

    /**
     * 选择头像选取途径对话框
     */
    private void showPictureSelectDialog() {
        if (pictureDialog == null)
            pictureDialog = new SelectPictureDialog(PersonalDataActivity.this);
        pictureDialog.setListener(new SelectPictureDialog.SelectPictureListener() {

            @Override
            public void selectCallback(int type) {
                //选择拍照
                if (type == 1) {
                    requestPermission(1);
                } else {//选择从相册上传
                    requestPermission(2);
                }
            }
        });
        pictureDialog.show();
    }

    /**
     * 初始化图片选择
     */
    private void initSelectPicture() {
        imageSelectUtils = new ImageSelectUtils(this);
        imageSelectUtils.setImageSelectCallBack(new ImageSelectUtils.OnImageSelectCallback() {
            @Override
            public void onImageSelected(ArrayList<String> paths) {
                String path = paths.get(0);
                //GlideUtils.loadCircleImage(PersonalDataActivity.this, mIvHeadProtrait, path);
                compressImage(path);
            }

            @Override
            public void onSystemImageSelect(String imageFilePath) {
            }
        });


    }

    /**
     * 请求相册访问权限
     *
     * @param type 1.拍照  2.相册
     */
    private void requestPermission(final int type) {
        mHelper = new PermissionHelper(this);
        mHelper.requestPermissions(getResources().getString(R.string.image_permission_first_hint),
                new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        if (type == 1) {
                            showCameraAction();
                        } else {
                            imageSelectUtils.openImageSelect(MultiImageSelectorActivity.MODE_SINGLE, 1, false);
                        }
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        mHelper.againWarnRequestPermission(getResources().getString(R.string.image_permission_hint), PersonalDataActivity.this);
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageSelectUtils.REQUEST_IMAGE) {//选择图片
            imageSelectUtils.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQUEST_CAMERA) { // 相机拍照完成后，返回图片路径
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    //相机拍照图片
                    //GlideUtils.loadCircleImage(PersonalDataActivity.this, mIvHeadProtrait, mTmpFile.getAbsolutePath());
                    compressImage(mTmpFile.getAbsolutePath());
                }
            } else {
                if (mTmpFile != null && mTmpFile.exists()) {
                    mTmpFile.delete();
                }
            }
        } else if (requestCode == REQUEST_NAME) {//修改姓名
            if (resultCode == 10) {
                String name = data.getStringExtra("content");
                mTvName.setText(name);
                new RxManager().post(RxConstant.RX_PERSON_DATA_UPDATE, 2);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mHelper != null)
            mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 相机拍照
     */
    private void showCameraAction() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(PersonalDataActivity.this.getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            File file = new File(Environment.getExternalStorageDirectory(), AppConstants.SD_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            mTmpFile = new File(file, fileName);
            Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", mTmpFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(PersonalDataActivity.this, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 压缩图片
     *
     * @param path
     */
    private void compressImage(final String path) {
        showRDialog();
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String comImage = ImageUtils.getInstance().compressPicture(path);
                        Message message = handler.obtainMessage();
                        message.obj = comImage;
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }).start();
    }

    //初始化页面弱引用的handler
    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<PersonalDataActivity> weakReference;

        public MyHandler(PersonalDataActivity context) {
            weakReference = new WeakReference<PersonalDataActivity>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            PersonalDataActivity activity = weakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        //开始上传文件
                        String imagePath = (String) msg.obj;
                        activity.updateHeadImage(imagePath);
                        break;
                }
            }
        }
    }

    /**
     * 上传头像
     *
     * @param path
     */
    private void updateHeadImage(String path) {
        File file = new File(path);
        OKHttpManager.commonUploadFile(URLConstant.UPLOAD_FILE, "file", file, new OKHttpManager.ResultCallback<BaseBean<HeadImageUploadBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseBean<HeadImageUploadBean> response) {
                String imagePath = response.getBody().getFileUrl();
                SpUtil.setHeadImage(imagePath);
                GlideUtils.loadCircleImage(PersonalDataActivity.this, mIvHeadProtrait, imagePath);
                new RxManager().post(RxConstant.RX_PERSON_DATA_UPDATE, 1);
                //更新用户资料
                updateUserInfo(imagePath);
            }
        }, this);
    }

    /**
     * 更新用户信息
     *
     * @param content
     */
    private void updateUserInfo(final String content) {
        params.clear();
        params.put("headPic", content);
        OKHttpManager.postJsonRequest(URLConstant.UPDATE_USER_INFO, params, new OKHttpManager.ResultCallback<BaseBean>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseBean response) {
                hideRDialog();

            }
        }, this);
    }
}
