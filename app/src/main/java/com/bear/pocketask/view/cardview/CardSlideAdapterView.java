package com.bear.pocketask.view.cardview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

/**
 * 卡片自动生成的view
 * Created by luoming on 10/12/2016.
 */
public class CardSlideAdapterView extends AdapterView
{
	private Adapter mAdapter; //适配器

	public CardSlideAdapterView(Context context)
	{
		this(context, null);
	}

	public CardSlideAdapterView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public CardSlideAdapterView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public Adapter getAdapter()
	{
		return mAdapter;
	}

	@Override
	public void setAdapter(Adapter adapter)
	{
        mAdapter = adapter;
	}

	@Override
	public View getSelectedView()
	{
		return null;
	}

	@Override
	public void setSelection(int position)
	{

	}
}
