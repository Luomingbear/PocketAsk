package com.bear.pocketask.view.cardview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by luoming on 10/9/2016.
 */
public class CardItem extends RelativeLayout
{
	private float mTouchX, mTouchY;

	public CardItem(Context context)
	{
		this(context, null);
	}

	public CardItem(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public CardItem(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		switch (ev.getAction() & ev.getActionMasked())
		{
		case MotionEvent.ACTION_DOWN:
		{
			mTouchX = ev.getX();
			mTouchY = ev.getY();
			break;
		}
		case MotionEvent.ACTION_MOVE:
		{
			float distance = Math.max(Math.abs(mTouchX - ev.getX()), Math.abs(mTouchY - ev.getY()));
			if (distance > 10)
				return true;
			break;
		}
		}
		return super.onInterceptTouchEvent(ev);
	}

	//	@Override
	//	public boolean dispatchTouchEvent(MotionEvent ev)
	//	{
	//		switch (ev.getAction() & ev.getActionMasked())
	//		{
	//		case MotionEvent.ACTION_DOWN:
	//		{
	//			mTouchX = ev.getX();
	//			mTouchY = ev.getY();
	//			break;
	//		}
	//		case MotionEvent.ACTION_MOVE:
	//		{
	//			float distance = Math.max(Math.abs(mTouchX - ev.getX()), Math.abs(mTouchY - ev.getY()));
	//			if (distance > 10)
	//				return false;
	//			break;
	//		}
	//		}
	//		return super.dispatchTouchEvent(ev);
	//	}
}
