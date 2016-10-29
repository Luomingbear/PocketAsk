package com.bear.pocketask.activity.person;

import android.os.Bundle;

import com.bear.pocketask.R;
import com.bear.pocketask.activity.base.BaseActivity;
import com.bear.pocketask.widget.titleview.TitleView;

/**
 * 个人中心
 * Created by bear on 16/10/29.
 */

public class PersonActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_layout);
        initView();
    }

    private void initView() {
        initTitleView();
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
}
