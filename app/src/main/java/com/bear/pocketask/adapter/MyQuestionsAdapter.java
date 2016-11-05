package com.bear.pocketask.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bear.pocketask.R;
import com.bear.pocketask.adapter.base.IBaseAdapter;
import com.bear.pocketask.info.MyQuestionInfo;

import java.util.List;

/**
 * 我的提问 adapter
 * Created by bear on 16/11/5.
 */

public class MyQuestionsAdapter extends IBaseAdapter {

    private ViewHolder viewHolder;

    public MyQuestionsAdapter(Context context, List<?> mList) {
        super(context, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getList().size() < 1)
            return null;

        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.question_item, null);

            viewHolder = new ViewHolder();
            viewHolder.question = (TextView) convertView.findViewById(R.id.question_item_question);
            viewHolder.remindCount = (TextView) convertView.findViewById(R.id.question_item_remind_count);
            convertView.setTag(viewHolder);
        } else convertView.getTag();

        //set values
        if (getItem(position) instanceof MyQuestionInfo) {
            MyQuestionInfo info = (MyQuestionInfo) getItem(position);

            viewHolder.question.setText(info.getQuestion());
            viewHolder.remindCount.setText(info.getIsPrivate() == 1 ? getmContext().getString(R.string.secret) : getmContext().getString(R.string.my_questions_remind_count, info.getRemindCount()));
        }

        return convertView;
    }

    private class ViewHolder {

        TextView question; //提问

        TextView remindCount; // 回复数量
    }
}
