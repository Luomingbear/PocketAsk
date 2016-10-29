package com.bear.pocketask.activity.login;

import android.os.Bundle;

import com.bear.pocketask.R;
import com.bear.pocketask.activity.base.BaseActivity;

/**
 * Created by bear on 16/10/29.
 */

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();
    }

    private void initView() {
    }
}
