package com.bear.pocketask.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bear.pocketask.R;
import com.bear.pocketask.adapter.base.IBaseAdapter;
import com.bear.pocketask.info.SelectorInfo;
import com.bear.pocketask.widget.selectorbutton.SelectorButton;

import java.util.List;

/**
 * 选项适配器
 * Created by bear on 16/10/20.
 */

public class SelectorAdapter extends IBaseAdapter {
    private Context mContext;

    public SelectorAdapter(Context context, List<?> mList) {
        super(context, mList);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//		SelectorButton selectorButton = new SelectorButton(mContext);
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		selectorButton.setLayoutParams(params);
//		SelectorInfo info = (SelectorInfo) getItem(position);
//		selectorButton.setTextString(info.getContent());

        if (getCount() == 0)
            return null;

        if (convertView == null) {
            viewHold = new ViewHold();

            convertView = getInflater().inflate(R.layout.choose_selector_item, null);

            viewHold.selectorButton = (SelectorButton) convertView.findViewById(R.id.choose_selector_button);

            convertView.setTag(viewHold);
        } else convertView.getTag();

        SelectorInfo info = (SelectorInfo) getItem(position);
        viewHold.selectorButton.setTextString(info.getContent());
        return convertView;
    }

    private ViewHold viewHold;

    private class ViewHold {
        SelectorButton selectorButton;
    }
}
