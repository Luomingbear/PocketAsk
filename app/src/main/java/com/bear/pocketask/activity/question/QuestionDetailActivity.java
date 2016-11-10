package com.bear.pocketask.activity.question;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bear.pocketask.R;
import com.bear.pocketask.activity.base.BaseActivity;
import com.bear.pocketask.adapter.CommentAdapter;
import com.bear.pocketask.adapter.SelectorAdapter;
import com.bear.pocketask.info.CardItemInfo;
import com.bear.pocketask.info.CommentInfo;
import com.bear.pocketask.info.SelectorInfo;
import com.bear.pocketask.utils.AdapterViewUtil;
import com.bear.pocketask.widget.inputview.ITextView;
import com.bear.pocketask.widget.record.RecordView;
import com.bear.pocketask.widget.selectorbutton.SelectorAdapterView;
import com.bear.pocketask.widget.titleview.TitleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题详情页 评论页
 * Created by bear on 16/11/1.
 */

public class QuestionDetailActivity extends BaseActivity implements View.OnClickListener {
    private ListView mLvComment; //评论列表
    private ViewHolder viewHolder;
    private CardItemInfo cardItemInfo; //卡片数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_detail_layout);

        initView();
    }

    private void initView() {
        cardItemInfo = getIntent().getParcelableExtra("card");
        if (cardItemInfo == null)
            cardItemInfo = new CardItemInfo();

        //title
        initTitleView();

        //comment listView
        initCommentListView();

        //设置问题卡片的数据
        initCard();

    }

    private void initTitleView() {
        TitleView titleView = (TitleView) findViewById(R.id.title_view);
        titleView.setmTitleText(cardItemInfo.getQuestions());
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

    private void initCommentListView() {
        mLvComment = (ListView) findViewById(R.id.question_detail_list_view);
        setCommentList();
    }

    /**
     * 设置评论数据
     */
    private void setCommentList() {

        List<CommentInfo> commentInfoList = new ArrayList<CommentInfo>();
        for (int i = 0; i < 5; i++) {
            CommentInfo commentInfo = new CommentInfo(i, "http://www.ld12.com/upimg358/allimg/c150627/14353W345a130-Q2B.jpg",
                    "JoyWang", "重庆的鸡公煲最好吃了重庆的鸡公煲最好吃了重庆的鸡公煲最好吃了＝ ＝ ＋" + i);
            commentInfoList.add(commentInfo);
        }

        CommentAdapter commentAdapter = new CommentAdapter(this, commentInfoList);
        mLvComment.setAdapter(commentAdapter);
        AdapterViewUtil.FixHeight(mLvComment);
    }

    private void initCard() {

        viewHolder = new ViewHolder();
        viewHolder.ivDetailPic = (ImageView) findViewById(R.id.card_item_detail_pic);
        viewHolder.ivHeadPic = (ImageView) findViewById(R.id.card_item_head_portrait);
        viewHolder.tvQuestion = (TextView) findViewById(R.id.card_item_questions_text);
        viewHolder.tvUserName = (TextView) findViewById(R.id.card_item_user_name);

        viewHolder.leReport = findViewById(R.id.card_item_report);
        viewHolder.rvRecordView = (RecordView) findViewById(R.id.card_item_questions_audio);
        viewHolder.llSelectorLayout = findViewById(R.id.card_item_select_list_layout);
        viewHolder.lvSelectorList = (SelectorAdapterView) findViewById(R.id.card_item_select_list);
        //
        //显示圆形的图像
        ImageLoader.getInstance().displayImage(cardItemInfo.getHeadPic(), viewHolder.ivHeadPic);
        //昵称
        viewHolder.tvUserName.setText(cardItemInfo.getUserName());
        setCardDate(viewHolder, cardItemInfo);
    }

    private void setCardDate(ViewHolder viewHolder, CardItemInfo info) {
        switch (info.getCardMode()) {
            case TopAudioBottomImage: {
                viewHolder.llSelectorLayout.setVisibility(View.GONE);
                viewHolder.tvQuestion.setVisibility(View.GONE);

                viewHolder.ivDetailPic.setVisibility(View.VISIBLE);
                viewHolder.rvRecordView.setVisibility(View.VISIBLE);

                //显示普通的矩形详情图片
                ImageLoader.getInstance().displayImage(info.getDetailPic(), viewHolder.ivDetailPic);
                //设置语音
                viewHolder.rvRecordView.setClickRf();
                break;
            }
            case TopAudioBottomSelector: {
                viewHolder.llSelectorLayout.setVisibility(View.VISIBLE);
                viewHolder.tvQuestion.setVisibility(View.GONE);

                viewHolder.ivDetailPic.setVisibility(View.GONE);
                viewHolder.rvRecordView.setVisibility(View.VISIBLE);

                //
                viewHolder.rvRecordView.setClickRf();
                initList(viewHolder.lvSelectorList, info.getSelectorList());
                break;
            }
            case TopTextBottomImage: {
                viewHolder.llSelectorLayout.setVisibility(View.GONE);
                viewHolder.tvQuestion.setVisibility(View.VISIBLE);

                viewHolder.ivDetailPic.setVisibility(View.VISIBLE);
                viewHolder.rvRecordView.setVisibility(View.GONE);

                //
                viewHolder.tvQuestion.setText(info.getQuestions());
                ImageLoader.getInstance().displayImage(info.getDetailPic(), viewHolder.ivDetailPic);
                break;
            }
            case TopTextBottomSelector: {
                viewHolder.llSelectorLayout.setVisibility(View.VISIBLE);
                viewHolder.tvQuestion.setVisibility(View.VISIBLE);

                viewHolder.ivDetailPic.setVisibility(View.GONE);
                viewHolder.rvRecordView.setVisibility(View.GONE);

                //
                viewHolder.tvQuestion.setText(info.getQuestions());
                initList(viewHolder.lvSelectorList, info.getSelectorList());
                break;
            }
        }

        //添加点击事件
        viewHolder.leReport.setOnClickListener(this);
        viewHolder.ivHeadPic.setOnClickListener(this);
        viewHolder.ivDetailPic.setOnClickListener(this);

    }

    /**
     * 设置选项数据
     *
     * @param listView
     * @param selectorList
     */
    private void initList(AdapterView listView, List<SelectorInfo> selectorList) {
        SelectorAdapter selectorAdapter = new SelectorAdapter(this, selectorList);
        listView.setAdapter(selectorAdapter);
        AdapterViewUtil.FixHeight(listView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_item_head_portrait: {
                break;
            }
            case R.id.card_item_detail_pic: {
                break;

            }
            case R.id.card_item_report: {
                break;

            }
        }
    }

    private class ViewHolder {
        ImageView ivHeadPic;
        TextView tvQuestion;
        TextView tvUserName;
        ImageView ivDetailPic;
        View btSend;
        View leReport;
        ITextView etInputView;
        RecordView rvRecordView;
        View llSelectorLayout;
        SelectorAdapterView lvSelectorList;
    }
}


