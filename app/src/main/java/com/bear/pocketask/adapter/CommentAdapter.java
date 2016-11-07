package com.bear.pocketask.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bear.pocketask.R;
import com.bear.pocketask.adapter.base.IBaseAdapter;
import com.bear.pocketask.info.CommentInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 评论适配器
 * Created by bear on 16/11/7.
 */

public class CommentAdapter extends IBaseAdapter {
    private ViewHolder viewHolder;

    public CommentAdapter(Context context, List<?> mList) {
        super(context, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = getInflater().inflate(R.layout.comment_item, null);

            viewHolder.headPortrait = (ImageView) convertView.findViewById(R.id.comment_head_portrait);
            viewHolder.username = (TextView) convertView.findViewById(R.id.comment_username);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.comment_comment_text);
            viewHolder.report = convertView.findViewById(R.id.comment_report);

            convertView.setTag(viewHolder);
        } else convertView.getTag();

        if (getItem(position) instanceof CommentInfo) {
            CommentInfo info = (CommentInfo) getItem(position);

            ImageLoader.getInstance().displayImage(info.getUHeadPortraitUrl(), viewHolder.headPortrait);
            viewHolder.username.setText(info.getUName());
            viewHolder.comment.setText(info.getComment_text());
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView headPortrait;

        TextView username;

        View report;

        TextView comment;
    }

}
