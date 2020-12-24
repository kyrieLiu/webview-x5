package com.jingkai.asset.function.login.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jingkai.asset.R;
import com.jingkai.asset.app.AppApplication;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.login.entity.LoginBean;
import com.jingkai.asset.function.main.activity.MainActivity;
import com.jingkai.asset.manager.ActivitiesManager;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.SpUtil;
import com.jingkai.asset.utils.ToastUtil;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liuyin on 2019/2/28 18:17
 *
 * @Describe 登录页
 */
public class LoginActivity extends BaseActivity {
    //手机号
    @Bind(R.id.et_login_account)
    EditText mEtAccount;
    //密码
    @Bind(R.id.et_login_password)
    EditText mEtPassword;
    //登录
    @Bind(R.id.bt_login_enter)
    Button mBtLogin;

    private String account;
    private String password;
    //是否为手机号的表达式
    private String REGEX_MOBILE = "^1\\d{10}$";

    /**
     * 调用该方法是应用内因token或主动退出登录,需要将栈内页面都销毁
     *
     * @param context
     */
    public static void start(Context context) {
        SpUtil.setIsLogin(false);
        Intent intent = new Intent(AppApplication.getInstance(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        AppApplication.getInstance().startActivity(intent);
        ActivitiesManager.getInstance().popOtherActivity(LoginActivity.class);
    }

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {//将导航栏设置为透明色
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Intent intent = getIntent();
        String action = intent.getAction();
        //从OA公众号网页跳转进来
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            String token = uri.getQueryParameter("token");
            String userName = uri.getQueryParameter("userName");
            mEtAccount.setText(userName);
            oaLogin(userName, token);
        } else {
            mEtAccount.setText(SpUtil.getLoginAcount());
            mEtPassword.setText(SpUtil.getLoginPassword());
        }
        mEtAccount.setSelection(mEtAccount.getText().length());
    }

    @Override
    protected void loadData() {
    }


    /**
     * OA用户登录
     */
    private void oaLogin(final String userName, String token) {
        params.clear();
        params.put("loginName", userName);
        params.put("token", token);
        params.put("os", 2);
        params.put("idEntity", 2);
        mBtLogin.setEnabled(false);
        showRDialog();
        OKHttpManager.postJsonNoToken(URLConstant.OA_LOGIN, params, new OKHttpManager.ResultCallback<BaseBean<LoginBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
                mBtLogin.setEnabled(true);
            }

            @Override
            public void onResponse(BaseBean<LoginBean> response) {
                hideRDialog();

                operateLoginData(response.getBody(), userName, "");
            }
        }, this);
    }


    @OnClick(R.id.bt_login_enter)
    public void onViewClicked() {
        final String account = mEtAccount.getText().toString();
        final String password = mEtPassword.getText().toString();
        if (TextUtils.isEmpty(account)) {
            ToastUtil.toastShortCenter("请输入账号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.toastShortCenter("请输入密码");
            return;
        }
        params.clear();
        params.put("username", account);
        params.put("password", password);
        params.put("imageCode", "HACD");
//        params.put("os", 2);
//        params.put("idEntity", 2);
        showRDialog();
        OKHttpManager.getAsyn(URLConstant.LOGIN, new OKHttpManager.ResultCallback<BaseBean<LoginBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseBean<LoginBean> response) {
                hideRDialog();
                LoginBean loginBean =response.getBody();
                if (loginBean.getApiResource()==null||loginBean.getApiResource().size()==0){
                    ToastUtil.toastShortCenter("当前账号暂无登录权限");
                    return;
                }
                operateLoginData(response.getBody(), account, password);
            }
        }, this);
    }


    /**
     * 处理登录返回数据
     */
    private void operateLoginData(LoginBean loginBean, String account, String password) {
        SpUtil.setIsLogin(true);
        SpUtil.setLoginAcount(account);
        SpUtil.setLoginPassword(password);
        SpUtil.setToken(loginBean.getToken());
        SpUtil.setJob(loginBean.getNameDepart());
        //存储权限CODE
        SpUtil.setPermissionCode(loginBean.getApiResource());
        LoginBean.UserBean userBean = loginBean.getUser();
        SpUtil.setName(userBean.getName());
        SpUtil.setPhone(userBean.getPhone());
        SpUtil.setHeadImage(userBean.getHeadPic());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
