package com.bear.pocketask.activity.create;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bear.pocketask.R;
import com.bear.pocketask.activity.base.BaseActivity;
import com.bear.pocketask.adapter.CreateSelectorAdapter;
import com.bear.pocketask.adapter.IViewPagerAdapter;
import com.bear.pocketask.utils.AdapterViewUtil;
import com.bear.pocketask.widget.inputview.InputDialog;
import com.bear.pocketask.widget.titleview.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建问题
 * Created by bear on 16/10/31.
 */

public class CreateQuestionActivity extends BaseActivity {
    private static final String TAG = "CreateQuestionActivity";

    private TextView mTextQuestion; //文本的问题标题
    private static int maxQuestionTextNum = 50; //文本的最大字数

    private List<String> mSelectorList; //选项数据
    private ListView mLv_question; //选项列表
    private CreateSelectorAdapter mSelectoradapter; //选项适配器
    private static int maxSelectorTextNum = 16; //选项文本的最大字数
    private int maxSelectorNum = 4; //最大的选项数量

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

    /**
     * 顶部的滚动view
     */
    private void initTopViewPager() {
        List<View> viewList = new ArrayList<View>();

        //编辑文本
        LayoutInflater lf = getLayoutInflater().from(this);

        View textQuestion = lf.inflate(R.layout.create_text_question_view, null);
        viewList.add(textQuestion);
        mTextQuestion = (TextView) viewList.get(0).findViewById(R.id.create_tv_question);
        mTextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestionInputDialog();
            }
        });

        //语音
        View recordQuestion = lf.inflate(R.layout.create_record_question_view, null);
        viewList.add(recordQuestion);

        IViewPagerAdapter viewPagerAdapter = new IViewPagerAdapter(viewList);
        ViewPager viewPagerTop = (ViewPager) findViewById(R.id.create_view_pager_top);
        viewPagerTop.setAdapter(viewPagerAdapter);

        //设置圆点的显示
        viewPagerTop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showViewPagerPoints(Gravity.TOP, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 显示问题标题的输入框
     */
    private void showQuestionInputDialog() {
        InputDialog inputDialog = new InputDialog(this);
        inputDialog.setShowText(mTextQuestion.getText().toString());
        inputDialog.init(new InputDialog.OnInputChangeListener() {
            @Override
            public void onTextChange(CharSequence s, int start, int before, int count) {
                if (start + count > maxQuestionTextNum) {
                    s = s.subSequence(0, maxQuestionTextNum);
                    Toast.makeText(getBaseContext(), getString(R.string.create_question_max_hint), Toast.LENGTH_SHORT).show();
                }

                mTextQuestion.setText(s.toString());
            }

            @Override
            public void onSendClick() {

            }
        });
    }

    /**
     * 底部的滚动view
     */
    private void initBottomViewPager() {
        List<View> viewList = new ArrayList<View>();

        //选项
        LinearLayout lv_layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lv_layout.setLayoutParams(params);
        lv_layout.setPadding(0, (int) getResources().getDimension(R.dimen.margin_normal), 0, 0);
        lv_layout.setGravity(Gravity.CENTER_VERTICAL);
        mLv_question = new ListView(this);
        mLv_question.setLayoutParams(params);
        mLv_question.setDivider(null);
        mLv_question.setFocusable(false);
        initSelector(mLv_question);

        //addView
        lv_layout.addView(mLv_question);
        viewList.add(lv_layout);
        //图片
        LayoutInflater lf = getLayoutInflater().from(this);
        View imageQuestion = lf.inflate(R.layout.create_picture_question_view, null);
        viewList.add(imageQuestion);

        IViewPagerAdapter viewPagerAdapter = new IViewPagerAdapter(viewList);
        ViewPager viewPagerBottom = (ViewPager) findViewById(R.id.create_view_pager_bottom);
        viewPagerBottom.setAdapter(viewPagerAdapter);

        //设置圆点的显示
        viewPagerBottom.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //当前在第几页
                showViewPagerPoints(Gravity.BOTTOM, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 设置圆点的显示
     *
     * @param gravity 圆点属于哪一个viewpager Gravity.TOP:上面 Gravity.BOTTOM:下面
     */
    private void showViewPagerPoints(int gravity, int position) {
        // TODO: 16/11/7  显示圆点的和变化
        RadioButton[] topPoints = new RadioButton[2];
        topPoints[0] = (RadioButton) findViewById(R.id.create_top_point1);
        topPoints[1] = (RadioButton) findViewById(R.id.create_top_point2);
        RadioButton[] bottomPoints = new RadioButton[2];
        bottomPoints[0] = (RadioButton) findViewById(R.id.create_bottom_point1);
        bottomPoints[1] = (RadioButton) findViewById(R.id.create_bottom_point2);

        switch (gravity) {
            case Gravity.TOP:
                topPoints[position].setChecked(true);
                break;
            case Gravity.BOTTOM:
                bottomPoints[position].setChecked(true);
                break;
        }
    }

    /**
     * 初始化选项设置一个显示的选项
     *
     * @param listView
     */
    private void initSelector(ListView listView) {
        mSelectorList = new ArrayList<String>();

        addSelector();

        inputSelectorText();

    }

    private void inputSelectorText() {
        mLv_question.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                showInputDialog(position);
            }
        });

    }

    private void showInputDialog(final int position) {
        final int i = position;
        InputDialog inputDialog = new InputDialog(this);
        inputDialog.setShowText(mSelectorList.get(position));
        inputDialog.init(new InputDialog.OnInputChangeListener() {
            @Override
            public void onTextChange(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "onTextChange: count++" + count);
                Log.i(TAG, "onTextChange: start++" + start);
                Log.i(TAG, "onTextChange: before++" + before);
                if (count + start > maxSelectorTextNum) {
                    s = s.subSequence(0, maxSelectorTextNum);
                    Toast.makeText(getBaseContext(), getString(R.string.create_selector_max_hint), Toast.LENGTH_SHORT).show();
                }
                mSelectorList.set(i, s.toString());
                invalidateSelectorListView();
            }

            @Override
            public void onSendClick() {

            }
        });
    }

    /**
     * 添加选项
     */
    private void addSelector() {
        if (mSelectorList.size() < maxSelectorNum) {
            mSelectorList.add("");
            invalidateSelectorListView();
        }
    }

    /**
     * 刷新选项数据
     */
    private void invalidateSelectorListView() {
        mSelectoradapter = new CreateSelectorAdapter(this, mSelectorList);
        mSelectoradapter.setOnAddSelectorListener(new CreateSelectorAdapter.OnAddSelectorListener() {
            @Override
            public void onAddClicked() {
                addSelector();
            }

            @Override
            public void onSubClicked(int position) {
                mSelectorList.remove(position);
                invalidateSelectorListView();
            }
        });
        mLv_question.setAdapter(mSelectoradapter);
        AdapterViewUtil.FixHeight(mLv_question);
    }

}
