package com.bear.pocketask.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bear.pocketask.R;
import com.bear.pocketask.adapter.base.IBaseAdapter;

import java.util.List;

/**
 * 创建问题的选项的adapter
 * * Created by bear on 16/11/7.
 */

public class CreateSelectorAdapter extends IBaseAdapter {
    private ViewHolder viewHolder;
    private int max = 4; //选项最大值

    public CreateSelectorAdapter(Context context, List<?> mList) {
        super(context, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.selector_item, null);

            viewHolder = new ViewHolder();
            viewHolder.index = (TextView) convertView.findViewById(R.id.selector_index);
            viewHolder.textview = (TextView) convertView.findViewById(R.id.selector_text);
            viewHolder.add = convertView.findViewById(R.id.selector_add);
            convertView.setTag(viewHolder);
        } else convertView.getTag();

        if (position != getCount() - 1 || position == max - 1)
            viewHolder.add.setVisibility(View.GONE);
        else viewHolder.add.setVisibility(View.VISIBLE);

        Object object = getItem(position);
        if (object instanceof String) {
            String info = (String) object;
            viewHolder.textview.setText(info);
            viewHolder.index.setText(getIndex(position));
        }
        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/11/7 点击之后添加选项
            }
        });

        return convertView;
    }

    private String getIndex(int position) {
        switch (position) {
            case 0:
                return "A.";
            case 1:
                return "B.";
            case 2:
                return "C.";
            case 3:
                return "D.";
        }
        return null;
    }

    private class ViewHolder {


        TextView index; // 序号

        TextView textview; // 显示文本

        View add; //添加按钮
    }
}
