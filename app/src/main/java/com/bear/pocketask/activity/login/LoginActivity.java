package com.bear.pocketask.activity.login;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.bear.pocketask.R;
import com.bear.pocketask.activity.base.BaseActivity;
import com.bear.pocketask.activity.receiver.ReceiverActivity;

/**
 * 登录界面
 * 测试账号 joywang ； 123456789
 * Created by bear on 16/10/29.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mUserName; //用户名
    private EditText mPassWord; //密码

    private View mNoRegister; //没有注册
    private View mForgotPassword; //忘记密码

    private View mLoginQQ, mLoginWechat, mLoginWebo; //登录qq，微信，微博

    private View mLoginFlag; //登录按钮

    private View mSkiLogin; //跳过登录


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //
        initView();
    }


    private TextWatcher usernameEditListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextWatcher passwordEditListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void initView() {

        //账号和密码
        mUserName = (EditText) findViewById(R.id.login_et_username);
        mUserName.addTextChangedListener(usernameEditListener);
        mPassWord = (EditText) findViewById(R.id.login_et_password);
        mPassWord.addTextChangedListener(passwordEditListener);

        //注册和找回密码
        mNoRegister = findViewById(R.id.login_no_register);
        mNoRegister.setOnClickListener(this);
        mForgotPassword = findViewById(R.id.login_forgot_password);
        mForgotPassword.setOnClickListener(this);


        //第三方登录
        mLoginQQ = findViewById(R.id.login_qq);
        mLoginWechat = findViewById(R.id.login_wechat);
        mLoginWebo = findViewById(R.id.login_webo);

        mLoginQQ.setOnClickListener(this);
        mLoginWechat.setOnClickListener(this);
        mLoginWebo.setOnClickListener(this);

        //登录按钮
        mLoginFlag = findViewById(R.id.login_flag);
        mLoginFlag.setOnClickListener(this);

        //跳过登录
        mSkiLogin = findViewById(R.id.login_skip_login);
        mSkiLogin.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_no_register:
                break;
            case R.id.login_forgot_password:
                break;
            case R.id.login_qq:
                break;
            case R.id.login_wechat:
                break;
            case R.id.login_webo:
                break;
            case R.id.login_flag:
                clickLoginFlag();
                break;
            case R.id.login_skip_login:
                setLoginPreferences(UserMode.TOURIST);
                intentWithFlag(ReceiverActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;
        }
    }

    /**
     * 点击了登录按钮
     */
    private void clickLoginFlag() {
        animationFlag();

        if (checkInputAccount()) {
            setLoginPreferences(UserMode.USER);
            intentWithFlag(ReceiverActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

    }

    /**
     * 检测当前的账号是否正确
     *
     * @return true 正确 false 错误
     */
    private boolean checkInputAccount() {

        /**
         *
         */
        String username, password;

        username = mUserName.getText().toString();

        password = mPassWord.getText().toString();

        // 测试账号
        if (username.equals("joywang") && password.equals("123456789")) {
            return true;
        }

        //// TODO: 16/10/31 请求服务器的账号数据
//        Toast.makeText(this, "账号或者密码错误", Toast.LENGTH_SHORT).show();
        return false;
    }


    /**
     * 登录按钮的动画效果，在读取服务器的时候都是拉下状态，等到服务器确定账号无误就回弹上去，然后切换到首页
     */
    private void animationFlag() {
        //flag上拉
        flagDown();

        //flag回弹
        flagUp();

    }

    /**
     * 旗子下拉
     */
    private void flagDown() {
        AccelerateInterpolator al = new AccelerateInterpolator();  //加速
        ObjectAnimator animator = ObjectAnimator.ofFloat(mLoginFlag, "translationY",
                0f, 50);
        animator.setInterpolator(al);
        animator.setDuration(200);
        animator.start();
    }

    /**
     * 旗子回弹
     */
    private void flagUp() {
        DecelerateInterpolator al = new DecelerateInterpolator(); //减速
        ObjectAnimator animator = ObjectAnimator.ofFloat(mLoginFlag, "translationY",
                50, 0f);
        animator.setInterpolator(al);
        animator.setDuration(200);
        animator.start();
    }

    /**
     * 显示错误的提示
     *
     * @param loginFailedType 错误类型 LOGIN_FAILED：登录错误 REGISTER_FAILED注册错误
     */
    private void showLoginFaildWarning(LoginFailedType loginFailedType) {
        View loginFailedWarningLayout = findViewById(R.id.login_warning_layout);
        TextView loginFailedWarningTextView = (TextView) findViewById(R.id.login_warning_text);

        switch (loginFailedType) {
            case LOGIN_FAILED:
                loginFailedWarningLayout.setVisibility(View.VISIBLE);
                loginFailedWarningTextView.setText(getString(R.string.login_login_failed));
                break;
            case REGISTER_FAILED:
                loginFailedWarningLayout.setVisibility(View.VISIBLE);
                loginFailedWarningTextView.setText(getString(R.string.login_register_failed));
                break;
        }
    }

    private enum LoginFailedType {
        //登录错误
        LOGIN_FAILED,

        //注册错误
        REGISTER_FAILED
    }

    /**
     * 设置为已经登录
     */
    private void setLoginPreferences(UserMode userMode) {
        SharedPreferences sharedPreferences = getSharedPreferences("loginLog", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (userMode) {
            case TOURIST:
                editor.putBoolean("isTourist", true);
                break;

            case USER:
                editor.putBoolean("isLogin", true);
                break;
        }
        editor.apply();
    }

    private enum UserMode {
        //注册用户
        USER,

        //游客
        TOURIST
    }
}
