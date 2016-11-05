package com.bear.pocketask.activity.my;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bear.pocketask.R;
import com.bear.pocketask.activity.base.BaseActivity;
import com.bear.pocketask.adapter.MyQuestionsAdapter;
import com.bear.pocketask.info.MyQuestionInfo;
import com.bear.pocketask.utils.AdapterViewUtil;
import com.bear.pocketask.utils.DipPxConversion;
import com.bear.pocketask.widget.titleview.TitleView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 我的回答页面
 * Created by bear on 16/11/5.
 */

public class MyRemindsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_questions_layout);
        initView();
    }

    private void initView() {
        initTitleView();

        setQuestionsList();
    }

    private void initTitleView() {
        TitleView titleView = (TitleView) findViewById(R.id.title_view);
        titleView.setmTitleText(getString(R.string.my_reminds_title));
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

    /**
     * 设置问题列表
     */
    private void setQuestionsList() {
        List<MyQuestionInfo> myQuestionInfoList = new ArrayList<MyQuestionInfo>();
        for (int i = 0; i < 5; i++) {
            MyQuestionInfo info = new MyQuestionInfo("你喜欢吃冰淇淋吗？", i, 0, new Date());
            myQuestionInfoList.add(info);
        }

        MyQuestionsAdapter myQuestionsAdapter = new MyQuestionsAdapter(this, myQuestionInfoList);

        //ListView
        ListView lv_question = (ListView) findViewById(R.id.my_questions_list_view);
        lv_question.setAdapter(myQuestionsAdapter);
        lv_question.setDividerHeight(DipPxConversion.dip2px(this, 3));
//        ColorDrawable drawable = new ColorDrawable(getResources().getColor(R.color.grayblue));
//        lv_question.setDivider(drawable);

        //时间
        TextView tv_createTime = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DipPxConversion.dip2px(this, 30));
        tv_createTime.setLayoutParams(params);
        tv_createTime.setGravity(Gravity.CENTER);
        tv_createTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
        tv_createTime.setTextColor(getResources().getColor(R.color.white));
        tv_createTime.setText("- 一周内 -");

        lv_question.addHeaderView(tv_createTime);
        AdapterViewUtil.FixHeight(lv_question);

    }
}
