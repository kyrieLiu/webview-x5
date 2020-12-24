package com.jingkai.asset.function.property.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.common.activity.FileBrowserActivity;
import com.jingkai.asset.config.AppConstants;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.login.activity.LoginActivity;
import com.jingkai.asset.function.manage.entity.GardenInfoBean;
import com.jingkai.asset.function.operation.entity.PropertyServiceStandardBean;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.LogUtil;
import com.jingkai.asset.utils.ToastUtil;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by liuyin on 2019/4/15 14:28
 * Describe: 物业服务标准详情页面
 */
public class PropertyWebActivity extends BaseActivity {

    @Bind(R.id.pb_webview_load)
    ProgressBar progressBar;
    @Bind(R.id.wv_web)
    WebView webView;

    private String imgurl = "";
    private String webUrl;
    private String id;

    public static void start(Context context, String url, String title,String id) {
        Intent intent = new Intent(context, FileBrowserActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_web_property;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        id = getIntent().getStringExtra("id");
        webUrl = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        setTitle(title);
        //webUrl = "https://vodkgeyttp8.vod.126.net/cloudmusic/IiUyIDAwMGA0IDQhNCAhMQ==/mv/5678292/3e508549183aadc029aec6d37cc32aac.mp4?wsSecret=6d33dad586e1a369c95826d622658fa0&wsTime=1560319430";
        //webUrl = "http://www.ytmp3.cn/down/73693.mp3";
        //webUrl = "http://ac.test.jingcaiwang.cn:18080/assetH5/index.html#/HouseRental?token=customerce031cec9e4f4793aced6dd30b1fe41c";

        Log.e("tag", "URL==" + webUrl);

    }

    @Override
    protected void loadData() {
        initWebView();
        webView.loadUrl(webUrl);
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }


    /**
     * 获取详情数据
     */
    private void requestDetailData(){
          params.clear();
          params.put("id",id);
          showRDialog();
          OKHttpManager.postJsonRequest(URLConstant.GET_DOC_DETAIL, params, new OKHttpManager.ResultCallback<BaseBean<PropertyServiceStandardBean.ItemsBean>>() {
              @Override
              public void onError(int code, String result, String message) {
                  hideRDialog();
                  ToastUtil.toastShortCenter(message);

              }

              @Override
              public void onResponse(BaseBean<PropertyServiceStandardBean.ItemsBean> response) {
                  hideRDialog();
                  LogUtil.e("00000000000000000===="+response.toString());
                  webView.loadUrl(response.getBody().getDocUrl());
              }
          },this);
    }



    public void initWebView() {

        WebSettings webSetting = webView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        //webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        // 当页面宽度大于WebView宽度时，缩小使页面宽度等于WebView宽度
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setGeolocationEnabled(true);
        // webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
//        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
//        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
//        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
//                .getPath());
//        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
//        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);


        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void h5_finish() {
                PropertyWebActivity.this.finish();
            }

            @JavascriptInterface
            public void h5_complete() {
                setResult(10);
                PropertyWebActivity.this.finish();
            }

            @JavascriptInterface
            public void token_past_due() {//token过期
                ToastUtil.toastShortCenter("token已过期,请重新登录");
                LoginActivity.start(PropertyWebActivity.this);
            }

            @JavascriptInterface
            public void processHTML(String html) {
                Log.e("tag", "html===" + html);
            }
        }, "android");

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());

    }

    // Web视图
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //加载完成后可以调用这个查看HTML源码
            //webView.loadUrl("javascript:android.processHTML(document.documentElement.outerHTML);");

        }
    }

    protected class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(GONE);
            } else if (progressBar.getVisibility() == GONE) {
                progressBar.setVisibility(VISIBLE);
            }
            progressBar.setProgress(newProgress);
        }


    }


    /***
     * 功能：长按图片保存到手机
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuItem.OnMenuItemClickListener handler = new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle() == "保存到手机") {
                    downLoadFile(imgurl);
                } else {
                    return false;
                }
                return true;
            }
        };
        if (v instanceof WebView) {
            WebView.HitTestResult result = ((WebView) v).getHitTestResult();
            if (result != null) {
                int type = result.getType();
                if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    imgurl = result.getExtra();
                    menu.setHeaderTitle("提示");
                    menu.add(0, v.getId(), 0, "保存到手机").setOnMenuItemClickListener(handler);
                }
            }
        }
    }


    /**
     * 下载文件
     */
    public void downLoadFile(String path) {
        File file = new File(Environment.getExternalStorageDirectory(), AppConstants.SD_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        File recordFile = new File(file, "cache.jpg");
        OKHttpManager.getInstance().getDownloadDelegate().downloadAsynWithProgress(path, recordFile, new OKHttpManager.ProgressCallback() {

            @Override
            protected void onProgressChanged(float percent, long length) {
            }

            @Override
            public void onDownloadFinish(String fileAbsolutePath) {
                ToastUtil.toastLongCenter("下载完成");
            }

            @Override
            public void onDownloadError(String error) {
            }
        }, this);
        //  download(path);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.stopLoading();
            //webView.clearHistory();
            webView.clearCache(true);
            //webView.pauseTimers();
            webView.destroy();
            webView = null;
        }

    }
}
