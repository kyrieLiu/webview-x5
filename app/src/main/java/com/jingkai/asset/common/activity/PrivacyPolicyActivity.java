package com.jingkai.asset.common.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.config.AppConstants;
import com.jingkai.asset.function.repair.entity.FileBean;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.FileUtil;
import com.jingkai.asset.utils.PermissionHelper;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.utils.word.WordUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/3/5 21:05
 * Description: 社区隐私政策
 */
public class PrivacyPolicyActivity extends BaseActivity {
    @Bind(R.id.webView_privacy_policy)
    WebView webView;

    private PermissionHelper mHelper;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_privacy_policy;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("新元社区APP隐私政策");
    }

    @Override
    protected void loadData() {
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(true);// 设置WebView可触摸放大缩小
        settings.setUseWideViewPort(true);

        requestPermission();//在打开word文件前先请求内存卡读写权限
    }


    /**
     * 请求内存卡读写权限
     */
    private void requestPermission() {
        mHelper = new PermissionHelper(this);
        mHelper.requestPermissions(getResources().getString(R.string.image_permission_first_hint),
                new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        String url = "http://pocketbook.document.jingcaiwang.cn/group1/M00/00/6B/rBMBOF0LLdSAGFtHAFXulOG0dBM72.docx";

                        FileBean fileBean=new FileBean();
                        fileBean.setAddr(url);
                        fileBean.setName("");
                        FileUtil.getInstance().openFile(fileBean,PrivacyPolicyActivity.this);
                        //downLoadFromNet(url);
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        mHelper.againWarnRequestPermission(getResources().getString(R.string.image_permission_hint), PrivacyPolicyActivity.this);
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    //授权回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mHelper != null)
            mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 打开用户隐私政策文件
     */
    private void openDocx(File docFile) {
        try {
            FileInputStream inputStream = new FileInputStream(docFile);
            File file = new File(Environment.getExternalStorageDirectory(), "xinyuan");
            if (!file.exists()) {
                file.mkdirs();
            }
            File targetFile = new File(file, "新元社区APP隐私政策1.docx");
            writeBytesToFile(inputStream, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downLoadFromNet(final String url) {

        try {
            File parentFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), AppConstants.SD_PATH);
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            final File fileN = new File(parentFile, FileUtil.getInstance().getFileName(url));
            if (!fileN.exists()) {
                fileN.createNewFile();
            }
            showRDialog();
            OKHttpManager.getInstance().getDownloadDelegate().downloadAsyn(url, fileN.getPath(), new OKHttpManager.ResultCallback<String>() {
                @Override
                public void onError(int code, String result, String message) {
                    ToastUtil.toastShortCenter(message);
                    hideRDialog();
                }

                @Override
                public void onResponse(String response) {
                    hideRDialog();
                    openDocx(fileN);
                }
            }, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将隐私政策文件流存储到File文件夹内,并打开文件
     *
     * @param is
     * @param file
     * @throws IOException
     */
    public void writeBytesToFile(InputStream is, File file) throws IOException {
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread = 0;
            fos = new FileOutputStream(file);
            while ((nbread = is.read(data)) > -1) {
                fos.write(data, 0, nbread);
            }
            fos.flush();
            WordUtil wu = new WordUtil(file.getAbsolutePath());
            webView.loadUrl("file:///" + wu.htmlPath);
        } catch (Exception ex) {
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }


}
