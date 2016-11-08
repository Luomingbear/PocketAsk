package com.bear.pocketask.activity.create;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bear.pocketask.R;
import com.bear.pocketask.activity.base.BaseActivity;
import com.bear.pocketask.activity.receiver.ReceiverActivity;
import com.bear.pocketask.adapter.SelectorAdapter;
import com.bear.pocketask.info.CardItemInfo;
import com.bear.pocketask.info.SelectorInfo;
import com.bear.pocketask.utils.AdapterViewUtil;
import com.bear.pocketask.widget.inputview.ITextView;
import com.bear.pocketask.widget.record.RecordView;
import com.bear.pocketask.widget.selectorbutton.SelectorAdapterView;
import com.bear.pocketask.widget.titleview.TitleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 预览页面
 * Created by bear on 16/11/8.
 */

public class PreviewActivity extends BaseActivity {
    private CardItemInfo mCardInfo; //数据来源

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_layout);
        initView();
    }

    private void initView() {
        mCardInfo = getIntent().getParcelableExtra("preview");

        initTitleView();

        initCardDate();
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
                intentWithFlag(ReceiverActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            }
        });
    }

    private void initCardDate() {
        ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        viewHolder.ivDetailPic = (ImageView) findViewById(R.id.card_item_detail_pic);
        viewHolder.ivHeadPic = (ImageView) findViewById(R.id.card_item_head_portrait);
        viewHolder.tvQuestion = (TextView) findViewById(R.id.card_item_questions_text);
        viewHolder.tvUserName = (TextView) findViewById(R.id.card_item_user_name);

        viewHolder.leReport = findViewById(R.id.card_item_report);
        viewHolder.rvRecordView = (RecordView) findViewById(R.id.card_item_questions_audio);
        viewHolder.llSelectorLayout = findViewById(R.id.card_item_select_list_layout);
        viewHolder.lvSelectorList = (SelectorAdapterView) findViewById(R.id.card_item_select_adapter_view);
        initView(viewHolder, mCardInfo);
    }

    /**
     * 初始化view
     *
     * @param viewHolder
     * @param info
     */
    private void initView(ViewHolder viewHolder, CardItemInfo info) {
        switch (info.getCardMode()) {
            case TopAudioBottomImage: {
                viewHolder.llSelectorLayout.setVisibility(View.GONE);
                viewHolder.tvQuestion.setVisibility(View.GONE);

                viewHolder.ivDetailPic.setVisibility(View.VISIBLE);
                viewHolder.rvRecordView.setVisibility(View.VISIBLE);

                //显示普通的矩形详情图片
                ImageLoader.getInstance().displayImage("file:/" + info.getDetailPic(), viewHolder.ivDetailPic);
                //设置语音
                //// TODO: 16/10/28  语音
                break;
            }
            case TopAudioBottomSelector: {
                viewHolder.llSelectorLayout.setVisibility(View.VISIBLE);
                viewHolder.tvQuestion.setVisibility(View.GONE);

                viewHolder.ivDetailPic.setVisibility(View.GONE);
                viewHolder.rvRecordView.setVisibility(View.VISIBLE);

                //
                //// TODO: 16/10/28  语音,选项
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
                ImageLoader.getInstance().displayImage("file:/" + info.getDetailPic(), viewHolder.ivDetailPic);
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
        //
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
