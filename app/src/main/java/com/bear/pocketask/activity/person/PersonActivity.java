package com.bear.pocketask.activity.person;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.bear.pocketask.R;
import com.bear.pocketask.activity.base.BaseActivity;
import com.bear.pocketask.activity.login.LoginActivity;
import com.bear.pocketask.activity.my.MyQuestionsActivity;
import com.bear.pocketask.activity.my.MyRespondsActivity;
import com.bear.pocketask.widget.titleview.TitleView;

/**
 * 个人中心
 * Created by bear on 16/10/29.
 */

public class PersonActivity extends BaseActivity implements View.OnClickListener {
    private View mMyQuestions;
    private View mMyRespond;
    private View mFeedBack;
    private View mAboutUs;
    private View mLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_layout);
        initView();
    }

    private void initView() {
        initTitleView();

        mMyQuestions = findViewById(R.id.person_my_questions);
        mMyQuestions.setOnClickListener(this);
        mMyRespond = findViewById(R.id.person_my_respond);
        mMyRespond.setOnClickListener(this);
        mFeedBack = findViewById(R.id.person_feedback);
        mFeedBack.setOnClickListener(this);
        mAboutUs = findViewById(R.id.person_about_us);
        mAboutUs.setOnClickListener(this);
        mLogOut = findViewById(R.id.person_logout);
        mLogOut.setOnClickListener(this);

    }

    private void initTitleView() {
        TitleView titleView = (TitleView) findViewById(R.id.title_view);
        titleView.setOnTitleViewListener(new TitleView.OnTitleViewListener() {
            @Override
            public void onLeftButton() {
                finish();
            }

            @Override
            public void onRightButton() {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_my_questions:
                intentTo(MyQuestionsActivity.class);
                break;
            case R.id.person_my_respond:
                intentTo(MyRespondsActivity.class);

                break;
            case R.id.person_feedback:
                break;
            case R.id.person_about_us:
                break;
            case R.id.person_logout:
                logout();
                break;
        }
    }

    /**
     * 退出账号
     */
    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginLog", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("isTourist", false);

        editor.putBoolean("isLogin", false);

        editor.apply();

        intentWithFlag(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

    }
}
