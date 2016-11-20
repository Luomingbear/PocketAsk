package com.bear.pocketask.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bear.pocketask.R;
import com.bear.pocketask.adapter.base.IBaseAdapter;
import com.bear.pocketask.info.CardItemInfo;
import com.bear.pocketask.info.SelectorInfo;
import com.bear.pocketask.tools.observable.EventObservable;
import com.bear.pocketask.utils.AdapterViewUtil;
import com.bear.pocketask.widget.inputview.ITextView;
import com.bear.pocketask.widget.record.RecordObservable;
import com.bear.pocketask.widget.record.RecordView;
import com.bear.pocketask.widget.selectorbutton.SelectorAdapterView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 生成没有输入框的卡片
 * Created by bear on 16/11/9.
 */

public class CardAdapter extends IBaseAdapter {
    private ViewHolder viewHolder;

    public CardAdapter(Context context, List<?> mList) {
        super(context, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.card_item, null);

            viewHolder = new ViewHolder();
            viewHolder.ivDetailPic = (ImageView) convertView.findViewById(R.id.card_item_detail_pic);
            viewHolder.ivHeadPic = (ImageView) convertView.findViewById(R.id.card_item_head_portrait);
            viewHolder.tvQuestion = (TextView) convertView.findViewById(R.id.card_item_questions_text);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.card_item_user_name);

            viewHolder.leReport = convertView.findViewById(R.id.card_item_report);
            viewHolder.rvRecordView = (RecordView) convertView.findViewById(R.id.card_item_questions_audio);
            viewHolder.llSelectorLayout = convertView.findViewById(R.id.card_item_select_list_layout);
            viewHolder.lvSelectorList = (SelectorAdapterView) convertView.findViewById(R.id.card_item_select_list);
            viewHolder.etInputView = (ITextView) convertView.findViewById(R.id.card_item_input);
            viewHolder.btSend = convertView.findViewById(R.id.card_item_send);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        //设置值
        CardItemInfo info = (CardItemInfo) getItem(position);

        //显示圆形的图像
        ImageLoader.getInstance().displayImage(info.getHeadPic(), viewHolder.ivHeadPic);
        //昵称
        viewHolder.tvUserName.setText(info.getUserName());


        viewHolder.tvQuestion.setText(info.getQuestions());
        //设置id
        viewHolder.rvRecordView.setRecordId(info.getQuestionId());
        RecordObservable.getInstance().addObserver(viewHolder.rvRecordView);
        viewHolder.etInputView.setQuestionId(info.getQuestionId());
        viewHolder.etInputView.setText(info.getInputText());
        EventObservable.getInstance().addObserver(viewHolder.etInputView);


        initView(viewHolder, info);


        return convertView;
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
                ImageLoader.getInstance().displayImage(info.getDetailPic(), viewHolder.ivDetailPic);
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
    }

    /**
     * 设置选项数据
     *
     * @param listView
     * @param selectorList
     */
    private void initList(AdapterView listView, List<SelectorInfo> selectorList) {
        SelectorAdapter selectorAdapter = new SelectorAdapter(getmContext(), selectorList);
        listView.setAdapter(selectorAdapter);
        AdapterViewUtil.FixHeight(listView);

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
