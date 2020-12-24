package com.jingkai.asset.function.login.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.utils.KeyboardHelper;
import com.jingkai.asset.utils.TimerTaskUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liuyin on 2019/2/20 15:04
 *
 * @Describe 验证码展示处理页面
 */
public class VerificationCodeActivity extends BaseActivity {

    @Bind(R.id.tv_verification_code_first)
    TextView mTvFirst;
    @Bind(R.id.iv_verification_code_first)
    ImageView mIvFirst;
    @Bind(R.id.tv_verification_code_second)
    TextView mTvSecond;
    @Bind(R.id.iv_verification_code_second)
    ImageView mIvSecond;
    @Bind(R.id.tv_verification_code_third)
    TextView mTvThird;
    @Bind(R.id.iv_verification_code_third)
    ImageView mIvThird;
    @Bind(R.id.tv_verification_code_forth)
    TextView mTvForth;
    @Bind(R.id.iv_verification_code_forth)
    ImageView mIvForth;
    @Bind(R.id.et_verification_content)
    EditText mEtContent;
    @Bind(R.id.tv_verification_code_again)
    TextView mTvSendAgain;
    @Bind(R.id.bt_verification_sendMessage)
    Button mBtConfirm;

    private TextView[] textViews;//存放展示验证码输入的TextView
    private ImageView[] imageViews;//存放验证码占位的ImageView

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_verification_code;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("登录");
    }

    @Override
    protected void loadData() {

        textViews = new TextView[]{mTvFirst, mTvSecond, mTvThird, mTvForth};
        imageViews = new ImageView[]{mIvFirst, mIvSecond, mIvThird, mIvForth};

        mEtContent.setFocusable(true);
        mEtContent.setFocusableInTouchMode(true);
        mEtContent.requestFocus();

        KeyboardHelper.getInstance().openKeyBoard(mEtContent);

        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String codes = mEtContent.getText().toString();
                if (codes.length() < 4) {
                    mBtConfirm.setEnabled(false);
                } else {//如果4位输入完毕,可以点击验证完成
                    mBtConfirm.setEnabled(true);
                }
                for (int i = 0; i < 4; i++) {
                    //将已经输入的验证码展示到对应的textView上,隐藏占位符,没有输入的只展示占位符
                    if (i < codes.length()) {
                        textViews[i].setText(String.valueOf(codes.charAt(i)));
                        imageViews[i].setVisibility(View.GONE);
                    } else {
                        textViews[i].setText("");
                        imageViews[i].setVisibility(View.VISIBLE);
                    }
                }
            }
        });

      startTimer();

    }

    /**
     * 开始计时
     */
    private void startTimer(){
        TimerTaskUtil.getInstance().startCountDown(60,new TimerTaskUtil.TimerTaskListener() {
            @Override
            public void onError(Throwable e) {
                mTvSendAgain.setEnabled(true);
            }

            @Override
            public void onCompleted() {
                mTvSendAgain.setEnabled(true);
                mTvSendAgain.setText("再次发送");
            }

            @Override
            public void onNext(Long aLong) {
                mTvSendAgain.setText("再次发送("+aLong+"s)");
                mTvSendAgain.setEnabled(false);
            }

        });
    }


    @OnClick({R.id.tv_verification_code_again, R.id.bt_verification_sendMessage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_verification_code_again:
                break;
            case R.id.bt_verification_sendMessage:
                break;
        }
    }
}
