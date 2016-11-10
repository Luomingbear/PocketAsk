package com.bear.pocketask.activity.login;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.bear.pocketask.R;
import com.bear.pocketask.activity.base.BaseActivity;
import com.bear.pocketask.activity.receiver.ReceiverActivity;
import com.bear.pocketask.utils.PasswordUtil;

/**
 * 登录界面
 * 测试账号 joywang ； 123456789
 * Created by bear on 16/10/29.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private EditText mUserName; //用户名
    private EditText mPassWord; //密码
    private EditText mVerfiyPassWord; //确认密码
    private View mVerfiyPassWordLayout; //确认密码区

    private View mHintLayout; //提示区
    private View mNoRegister; //没有注册
    private View mForgotPassword; //忘记密码

    private View mThirdParty; //第三方登录布局
    private View mLoginQQ, mLoginWechat, mLoginWebo; //登录qq，微信，微博

    private View mLoginFlag; //登录按钮

    private View mSkiLogin; //跳过登录

    private boolean isRegisterLayout = false; //当前是否是注册账号页面


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
        mVerfiyPassWord = (EditText) findViewById(R.id.login_et_verify_password);
        //确认密码区
        mVerfiyPassWordLayout = findViewById(R.id.login_verify_password_layout);

        //注册和找回密码
        mHintLayout = findViewById(R.id.login_hint_layout);

        mNoRegister = findViewById(R.id.login_no_register);
        mNoRegister.setOnClickListener(this);
        mForgotPassword = findViewById(R.id.login_forgot_password);
        mForgotPassword.setOnClickListener(this);


        //第三方登录
        mThirdParty = findViewById(R.id.login_third_party);

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
                isRegisterLayout = true;
                showRegisterLayout(isRegisterLayout);
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
                intentWithFlag(ReceiverActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
        }
    }

    /**
     * 设置显示注册账号的页面
     *
     * @param isShow 是否显示 真：注册账号 假：登录
     */
    private void showRegisterLayout(boolean isShow) {
        if (isShow) {
            mVerfiyPassWordLayout.setVisibility(View.VISIBLE);
            mHintLayout.setVisibility(View.GONE);
            mThirdParty.setVisibility(View.GONE);
        } else {
            mVerfiyPassWordLayout.setVisibility(View.GONE);
            mHintLayout.setVisibility(View.VISIBLE);
            mThirdParty.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 点击了登录按钮
     */
    private void clickLoginFlag() {
        animationFlag();

        if (checkInputAccount()) {
            setLoginPreferences(UserMode.USER);
            intentWithFlag(ReceiverActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
        String username, password, verifyPassword = "";

        username = mUserName.getText().toString();

        password = mPassWord.getText().toString();

        // 测试账号
        if (username.equals("joywang") && password.equals("123456789")) {
            return true;
        }

        Log.i(TAG, "checkInputAccount: 加密密码：" + PasswordUtil.getEncodeUsernamePassword(username, password));

        //登录名或者密码为空
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
            showLoginFailedWarning(LoginFailedType.EMPTY_INPUT);
        else hideLoginFailedWarning();

        //注册账号的验证密码
        if (isRegisterLayout)
            verifyPassword = mVerfiyPassWord.getText().toString();

        //密码和确认密码不匹配
        if (!password.equals(verifyPassword) && isRegisterLayout) {
            showLoginFailedWarning(LoginFailedType.REGISTER_FAILED);
        }

        //登录名或者密码错误
        //// TODO: 16/10/31 请求服务器的账号数据
        if (requestServerLogin(username, password))
            return true;

        //没有注册


        return false;
    }

    /**
     * 请求服务器的数据验证账号
     *
     * @param username
     * @param password
     * @return 用户名和账号是否匹配
     */
    private boolean requestServerLogin(String username, String password) {
        // TODO: 16/11/10 验证用户名和账号是否匹配
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
    private void showLoginFailedWarning(LoginFailedType loginFailedType) {
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
            case EMPTY_INPUT:
                loginFailedWarningLayout.setVisibility(View.VISIBLE);
                loginFailedWarningTextView.setText(getString(R.string.login_empty_input));
                break;
        }
    }

    /**
     * 隐藏错误显示
     */
    public void hideLoginFailedWarning() {
        View loginFailedWarningLayout = findViewById(R.id.login_warning_layout);
        TextView loginFailedWarningTextView = (TextView) findViewById(R.id.login_warning_text);
        loginFailedWarningTextView.setText("");
        loginFailedWarningLayout.setVisibility(View.GONE);
    }

    private enum LoginFailedType {
        //登录错误
        LOGIN_FAILED,

        //注册错误
        REGISTER_FAILED,

        //登录名或密码为空
        EMPTY_INPUT
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
