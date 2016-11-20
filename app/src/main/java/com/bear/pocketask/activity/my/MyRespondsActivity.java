package com.bear.pocketask.activity.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bear.pocketask.R;
import com.bear.pocketask.activity.base.BaseActivity;
import com.bear.pocketask.activity.question.QuestionDetailActivity;
import com.bear.pocketask.adapter.MyQAAdapter;
import com.bear.pocketask.info.MyQAInfo;
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

public class MyRespondsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_questions_layout);
        initView();
    }

    private void initView() {
        initTitleView();

        setRespondList();
    }

    private void initTitleView() {
        TitleView titleView = (TitleView) findViewById(R.id.title_view);
        titleView.setmTitleText(R.string.my_reminds_title);
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
     * 设置回答列表
     */
    private void setRespondList() {
        List<MyQAInfo> myQAInfoList = new ArrayList<MyQAInfo>();
        for (int i = 0; i < 5; i++) {
            MyQAInfo info = new MyQAInfo("你喜欢吃冰淇淋吗？", i, 0, new Date(), MyQAInfo.QAType.RESPOND);
            myQAInfoList.add(info);
        }

        MyQAAdapter myRespondsAdapter = new MyQAAdapter(this, myQAInfoList);

        //listView外面的linearLayout
        LinearLayout listLayout = (LinearLayout) findViewById(R.id.my_questions_list_layout);

        listLayout.addView(createListView(myRespondsAdapter, "- 一周内 -"));
    }

    /**
     * 创建listview
     *
     * @param adapter    adapter
     * @param headerText header的文本，如果为null则不显示
     * @return
     */
    private ListView createListView(BaseAdapter adapter, String headerText) {
        ListView listView = new ListView(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(p);

        listView.setAdapter(adapter);
        listView.setDividerHeight(DipPxConversion.dip2px(this, 3));

        //时间
        if (!TextUtils.isEmpty(headerText)) {
            TextView tv_createTime = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DipPxConversion.dip2px(this, 30));
            tv_createTime.setLayoutParams(params);
            tv_createTime.setGravity(Gravity.CENTER);
            tv_createTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
            tv_createTime.setTextColor(getResources().getColor(R.color.white));
            tv_createTime.setText(headerText);
            listView.addHeaderView(tv_createTime);
        }

        AdapterViewUtil.FixHeight(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intentTo(QuestionDetailActivity.class);
            }
        });
        return listView;
    }
}
