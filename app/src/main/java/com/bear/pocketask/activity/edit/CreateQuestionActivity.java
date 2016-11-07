package com.bear.pocketask.activity.edit;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bear.pocketask.R;
import com.bear.pocketask.activity.base.BaseActivity;
import com.bear.pocketask.adapter.CreateSelectorAdapter;
import com.bear.pocketask.adapter.IViewPagerAdapter;
import com.bear.pocketask.utils.AdapterViewUtil;
import com.bear.pocketask.widget.titleview.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建问题
 * Created by bear on 16/10/31.
 */

public class CreateQuestionActivity extends BaseActivity {
    private List<String> mSelectorList; //选项数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_question_layout);

        initView();
    }

    private void initView() {

        initTitleView();

        initViewPager();
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

    private void initViewPager() {
        initTopViewPager();

        initBottomViewPager();
    }

    private void initTopViewPager() {
        List<View> viewList = new ArrayList<View>();

        //编辑文本
        LayoutInflater lf = getLayoutInflater().from(this);

        View textQuestion = lf.inflate(R.layout.create_text_question_view, null);
        viewList.add(textQuestion);

        //语音
        View recordQuestion = lf.inflate(R.layout.create_record_question_view, null);
        viewList.add(recordQuestion);

        IViewPagerAdapter viewPagerAdapter = new IViewPagerAdapter(viewList);
        ViewPager viewPagerTop = (ViewPager) findViewById(R.id.create_view_pager_top);
        viewPagerTop.setAdapter(viewPagerAdapter);
    }

    private void initBottomViewPager() {
        List<View> viewList = new ArrayList<View>();

        //选项
        LinearLayout lv_layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lv_layout.setLayoutParams(params);
        lv_layout.setPadding(0, (int) getResources().getDimension(R.dimen.margin_normal), 0, 0);
        lv_layout.setGravity(Gravity.CENTER_VERTICAL);
        ListView lv_question = new ListView(this);
        lv_question.setLayoutParams(params);
        lv_question.setDivider(null);
        lv_question.setFocusable(false);
        initSelector(lv_question);

        //addView
        lv_layout.addView(lv_question);
        viewList.add(lv_layout);
        //图片
        LayoutInflater lf = getLayoutInflater().from(this);
        View imageQuestion = lf.inflate(R.layout.create_picture_question_view, null);
        viewList.add(imageQuestion);

        IViewPagerAdapter viewPagerAdapter = new IViewPagerAdapter(viewList);
        ViewPager viewPagerTop = (ViewPager) findViewById(R.id.create_view_pager_bottom);
        viewPagerTop.setAdapter(viewPagerAdapter);
    }

    /**
     * 初始化选项设置一个显示的选项
     *
     * @param listView
     */
    private void initSelector(ListView listView) {
        mSelectorList = new ArrayList<String>();
        mSelectorList.add(getString(R.string.create_selector_hint));

        CreateSelectorAdapter adapter = new CreateSelectorAdapter(this, mSelectorList);
        listView.setAdapter(adapter);
        AdapterViewUtil.FixHeight(listView);
    }

    private void showViewPagerPoints() {
        // TODO: 16/11/7  显示圆点的和变化
    }

}
