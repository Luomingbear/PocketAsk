package com.bear.pocketask.activity.comment;

import android.os.Bundle;

import com.bear.pocketask.R;
import com.bear.pocketask.activity.base.BaseActivity;
import com.bear.pocketask.widget.titleview.TitleView;

/**
 * 问题详情页 评论页
 * Created by bear on 16/11/1.
 */

public class CommentActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_detail_layout);

        initView();
    }

    private void initView() {

        initTitleView();
    }

    private void initTitleView() {
        TitleView titleView = (TitleView) findViewById(R.id.title_view);
        titleView.setmTitleText("XXX");
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
