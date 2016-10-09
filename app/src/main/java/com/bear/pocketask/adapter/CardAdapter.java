package com.bear.pocketask.adapter;

import java.util.List;

import com.bear.pocketask.R;
import com.bear.pocketask.info.CardItemInfo;
import com.bear.pocketask.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 卡片信息适配器
 * Created by bear on 16/10/7.
 */

public class CardAdapter extends BaseAdapter implements View.OnClickListener {
    private static final String TAG = "CardAdapter";
    private List<CardItemInfo> list; //数据源列表
    CardItemInfo info;
    private LayoutInflater inflater;

    private float mTouchX, mTouchY;

    public CardAdapter(Context context, List<CardItemInfo> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list == null ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.card_item, null);

            viewHolder = new ViewHolder();
            viewHolder.ivDetailPic = (ImageView) convertView.findViewById(R.id.card_item_detail_pic);
            viewHolder.ivHeadPic = (CircleImageView) convertView.findViewById(R.id.card_item_head_pic);
            viewHolder.tvQuestion = (TextView) convertView.findViewById(R.id.card_item_questions);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.card_item_user_name);

            viewHolder.leReport = convertView.findViewById(R.id.card_item_report);
            viewHolder.btSend = convertView.findViewById(R.id.card_item_send);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        //设置值
        info = list.get(position);
        viewHolder.tvUserName.setText(info.getUserName());
        viewHolder.tvQuestion.setText(info.getQuestions());
        ImageLoader.getInstance().displayImage(info.getHeadPic(), viewHolder.ivHeadPic);
        ImageLoader.getInstance().displayImage(info.getDetailPic(), viewHolder.ivDetailPic);

        //添加点击事件
        viewHolder.leReport.setOnClickListener(this);
        viewHolder.ivDetailPic.setOnClickListener(this);
        viewHolder.ivHeadPic.setOnClickListener(this);
        //        viewHolder.btSend.setOnTouchListener(this);
        viewHolder.btSend.setOnClickListener(this);
        return convertView;
    }

    private ViewHolder viewHolder;
    private boolean isMove = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_item_head_pic: {
                if (cardItemClickListener != null)
                    cardItemClickListener.onClickedObject(CardItemClickMode.HEAD_PIC, info.getQuestionId());
                Log.i(TAG, "onTouch: down head pic");
                break;
            }
            case R.id.card_item_detail_pic: {
                if (cardItemClickListener != null)
                    cardItemClickListener.onClickedObject(CardItemClickMode.DETAIL_PIC, info.getQuestionId());
                Log.i(TAG, "onTouch: down detail pic");
                break;

            }
            case R.id.card_item_report: {
                if (cardItemClickListener != null)
                    cardItemClickListener.onClickedObject(CardItemClickMode.Report_BUTTON, info.getQuestionId());
                Log.i(TAG, "onClick: leReport");
                break;

            }
            case R.id.card_item_send: {
                if (cardItemClickListener != null)
                    cardItemClickListener.onClickedObject(CardItemClickMode.SEND_BUTTON, info.getQuestionId());
                Log.i(TAG, "onClick: send");
                break;
            }
        }
    }

    private class ViewHolder {
        CircleImageView ivHeadPic;
        TextView tvQuestion;
        TextView tvUserName;
        ImageView ivDetailPic;
        View btSend;
        View leReport;
    }

    private CardItemClickListener cardItemClickListener;

    public interface CardItemClickListener {
        void onClickedObject(CardItemClickMode clickMode, int questionId);
    }

    public void setCardItemClickListener(CardItemClickListener cardItemClickListener) {
        this.cardItemClickListener = cardItemClickListener;
    }

    public enum CardItemClickMode {
        //头像
        HEAD_PIC,

        //问题图片详情
        DETAIL_PIC,

        ///举报按钮
        Report_BUTTON,

        //发送按钮
        SEND_BUTTON,
    }
}
