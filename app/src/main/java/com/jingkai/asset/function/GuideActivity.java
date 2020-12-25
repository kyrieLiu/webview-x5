package com.jingkai.asset.function;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jingkai.asset.R;
import com.jingkai.asset.common.activity.SimpleActivity;
import com.jingkai.asset.function.login.activity.LoginActivity;
import com.jingkai.asset.function.main.activity.MainActivity;
import com.jingkai.asset.utils.SpUtil;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import java.util.HashMap;

/**
 * Created by liuyin on 2019/6/16 15:41
 * Describe: 启动页
 */
public class GuideActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        setContentView(R.layout.activity_guide);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideActivity.this, SimpleActivity.class);
                startActivity(intent);
            }
        });

        initX5();

    }

    /**
     * 初始化X5浏览器内核
     */
    private void initX5() {
        boolean b = QbSdk.canLoadX5(getApplicationContext());
        Log.d("mytag", "是否可以加载X5内核 -->" + b);
        if (b){
            Intent intent = new Intent(GuideActivity.this, SimpleActivity.class);
            startActivity(intent);
        }else{
            QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

                @Override
                public void onViewInitFinished(boolean arg0) {
                    //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                    Log.d("mytag", " onViewInitFinished is " + arg0);
                    Intent intent = new Intent(GuideActivity.this, SimpleActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onCoreInitFinished() {
                    Log.d("mytag", " onCoreInitFinished is ");
                }
            };
            //x5内核初始化接口
            QbSdk.initX5Environment(getApplicationContext(), cb);
        }
     /*   HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.d("mytag", "onDownloadFinish -->下载X5内核完成：" + i);
            }

            @Override
            public void onInstallFinish(int i) {
                Log.d("mytag", "onInstallFinish -->安装X5内核进度：" + i);
//                                boolean b = QbSdk.canLoadX5(getApplicationContext());
//                                Log.d("mytag", "onInstallFinish  是否可以加载X5内核 -->" + b);
                // initX5();
//
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.d("mytag", "onDownloadProgress -->下载X5内核进度：" + i);
            }
        });*/
    }


    /**
     * 开始跳转
     */
    private void intentStart() {
        Intent intent = new Intent();
        if (SpUtil.getIsLogin()) {
            intent.setClass(this, MainActivity.class);
        } else {
            intent.setClass(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }


}
