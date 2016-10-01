package com.bear.pocketask.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bear.pocketask.R;
import com.bear.pocketask.info.CardItemInfo;
import com.bear.pocketask.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 卡片信息适配器
 * Created by bear on 16/10/7.
 */

public class CardAdapter extends BaseAdapter {

    private List<CardItemInfo> list; //数据源列表
    private LayoutInflater inflater;

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
//            viewHolder.tvQuestion = (TextView) convertView.findViewById(R.id.);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.card_item_user_name);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        CardItemInfo info = list.get(position);
        viewHolder.tvUserName.setText(info.getUserName());
        ImageLoader.getInstance().displayImage(info.getHeadPic(), viewHolder.ivHeadPic);
        ImageLoader.getInstance().displayImage(info.getDetailPic(), viewHolder.ivDetailPic);

        return convertView;
    }

    private ViewHolder viewHolder;

    private class ViewHolder {
        CircleImageView ivHeadPic;
        TextView tvQuestion;
        TextView tvUserName;
        ImageView ivDetailPic;
    }
}
