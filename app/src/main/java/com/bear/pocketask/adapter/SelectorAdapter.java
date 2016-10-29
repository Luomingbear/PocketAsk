package com.bear.pocketask.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bear.pocketask.adapter.base.IBaseAdapter;
import com.bear.pocketask.info.SelectorInfo;
import com.bear.pocketask.widget.selectorbutton.SelectorButton;

import java.util.List;

/**
 * 选项适配器
 * Created by bear on 16/10/20.
 */

public class SelectorAdapter extends IBaseAdapter
{
	private Context mContext;

	public SelectorAdapter(Context context, List<?> mList)
	{
		super(context, mList);
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		SelectorButton selectorButton = new SelectorButton(mContext);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		selectorButton.setLayoutParams(params);
		SelectorInfo info = (SelectorInfo) getItem(position);
		selectorButton.setTextString(info.getContent());

		return selectorButton;
	}

	private ViewHold viewHold;

	private class ViewHold
	{
		SelectorButton selectorButton;
	}
}
