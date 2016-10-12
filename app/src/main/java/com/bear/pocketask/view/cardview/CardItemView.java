package com.bear.pocketask.view.cardview;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 卡片布局
 * 自定义拦截touch事件
 * Created by luoming on 10/9/2016.
 */
public class CardItemView extends RelativeLayout
{
	private static final String TAG = "CardItemView";
	private Spring mSpringX, mSpringY; //反弹
	private double mTension = 25; //拉力
	private double mFriction = 3; //摩擦力
	private float mTouchX, mTouchY; //手指按下时的坐标
	private float mDistanceX = 0, mDistanceY = 0; //手指移动的总距离
	private float mPosX = 0, mPosY = 0; //卡片的初始坐标
	private float mMaxRotation = 5; //限制卡片旋转的角度

	private float mCardHalfWidth = 0; //卡片的宽度的1/4

	private boolean isClicked = false;

	public CardItemView(Context context)
	{
		this(context, null);
	}

	public CardItemView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public CardItemView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initSpring();
	}

	private void initSpring()
	{
		SpringConfig config = SpringConfig.fromOrigamiTensionAndFriction(mTension, mFriction);
		SpringSystem system = SpringSystem.create();

		mSpringX = system.createSpring();
		mSpringX.setSpringConfig(config);
		mSpringX.addListener(new SpringListener()
		{
			@Override
			public void onSpringUpdate(Spring spring)
			{
				float x = (float) spring.getCurrentValue();
				setX(x);
				float rotation = x * mMaxRotation / 360;
				setRotation(rotation);

			}

			@Override
			public void onSpringAtRest(Spring spring)
			{

			}

			@Override
			public void onSpringActivate(Spring spring)
			{

			}

			@Override
			public void onSpringEndStateChange(Spring spring)
			{

			}
		});
		mSpringY = system.createSpring();
		mSpringY.setSpringConfig(config);
		mSpringY.addListener(new SpringListener()
		{
			@Override
			public void onSpringUpdate(Spring spring)
			{
				float y = (float) spring.getCurrentValue();
				setY(y);
			}

			@Override
			public void onSpringAtRest(Spring spring)
			{

			}

			@Override
			public void onSpringActivate(Spring spring)
			{

			}

			@Override
			public void onSpringEndStateChange(Spring spring)
			{

			}
		});

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		float distance;
		switch (ev.getAction() & ev.getActionMasked())
		{
		case MotionEvent.ACTION_DOWN:
			mTouchX = ev.getX();
			mTouchY = ev.getY();
			//获取当前的卡片位置，因为onTouchEvent可能会收不到touchDown事件，所以在这里获取
			if (mPosX == 0 || mPosY == 0)
			{
				mPosX = getX();
				mPosY = getY();
			}
			//得到卡片的宽度
			if (mCardHalfWidth == 0)
				mCardHalfWidth = getWidth() / 4;
			break;

		case MotionEvent.ACTION_MOVE:
			//如果移动的距离大于5 则拦截触摸事件，这样图片的点击事件就不能执行
			distance = Math.max(Math.abs(mTouchX - ev.getX()), Math.abs(mTouchY - ev.getY()));
			if (distance > 5)
				return true;
			break;
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction() & event.getActionMasked())
		{
		case MotionEvent.ACTION_DOWN:
		{
			//移动值设置为初始位置
			mDistanceX = mPosX;
			mDistanceY = mPosY;
			break;
		}
		case MotionEvent.ACTION_MOVE:
		{
			//计算卡片的位置
			mDistanceX += event.getX() - mTouchX;
			mDistanceY += event.getY() - mTouchY;

			//使弹簧静止
			mSpringX.setAtRest();
			mSpringY.setAtRest();

			//设置卡片跟随手指位置
			setX(mDistanceX);
			setY(mDistanceY);

			//设置卡片的旋转
			float rotation = mDistanceX * mMaxRotation / 360;
			setRotation(rotation);

			break;
		}
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
		{
			//设置滑动动画
			if (mDistanceX < mPosX - mCardHalfWidth)
				setSlideLeft();
			else if (mDistanceX > mPosX + mCardHalfWidth)
				setSlideRight();
			else
				setSlideBack();

			//设置是否是点击效果
			if (Math.max(Math.abs(mDistanceX - mPosX), Math.abs(mDistanceY - mPosY)) > 5)
				isClicked = false;
			else
				isClicked = true;

			//点击事件的回调
			if (event.getX() > 0 && event.getX() < getWidth())
				if (event.getY() > 0 && event.getY() < getHeight())
					if (onCardSlideListener != null && isClicked)
						onCardSlideListener.onClicked();

			//重置坐标
			mDistanceX = mPosX;
			mDistanceY = mPosY;

			break;
		}
		}
		return true;
	}

	/**
	 * 自动滚动回原处
	 */
	public void setSlideBack()
	{
		mSpringX.setCurrentValue(mDistanceX);
		mSpringX.setEndValue(mPosX);
		mSpringY.setCurrentValue(mDistanceY);
		mSpringY.setEndValue(mPosY);
	}

	/**
	 * 设置自动滑出左边界
	 */
	public void setSlideLeft()
	{
		mSpringX.setCurrentValue(getX());
		mSpringX.setEndValue(-getWidth() * 1.5);

		mSpringY.setCurrentValue(getY());
		//设置离开的y坐标，根据当前手指的滑动计算
		double y;
		if (mDistanceY == 0 || mDistanceX == 0)
			y = getWidth();
		y = ((mDistanceY - mPosY) * (mPosX + getWidth()) / Math.abs(mDistanceX - mPosX));
		mSpringY.setEndValue(y);

		if (onCardSlideListener != null)
		{
			onCardSlideListener.onExitLeft();
			onCardSlideListener.onRemove();
		}
	}

	/**
	 * 自动滑出右边界
	 */
	public void setSlideRight()
	{
		mSpringX.setCurrentValue(getX());
		mSpringX.setEndValue(getWidth() * 1.5);

		mSpringY.setCurrentValue(getY());
		//设置离开的y坐标，根据当前手指的滑动计算
		double y;
		if (mDistanceY == 0 || mDistanceX == 0)
			y = getWidth();
		y = ((mDistanceY - mPosY) * (mPosX + getWidth()) / Math.abs(mDistanceX - mPosX));
		mSpringY.setEndValue(y);

		if (onCardSlideListener != null)
		{
			onCardSlideListener.onExitRight();
			onCardSlideListener.onRemove();
		}
	}

	private onCardSlidingListener onCardSlideListener;

	public void setOnCardSlideListener(onCardSlidingListener onCardSlideListener)
	{
		this.onCardSlideListener = onCardSlideListener;
	}

	public interface onCardSlidingListener
	{
		void onExitLeft();

		void onExitRight();

		void onRemove();

		void onClicked();
	}
}
