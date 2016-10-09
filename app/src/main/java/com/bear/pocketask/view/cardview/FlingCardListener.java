package com.bear.pocketask.view.cardview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * 触摸事件的响应
 * Created by bear on 16/10/7.
 */

public class FlingCardListener implements View.OnTouchListener
{
	private static final String TAG = "FlingCardListener";
	private static final int INVALID_POINTER_ID = -1;

	private final float objectX; //x坐标
	private final float objectY; //y坐标
	private final int objectH; //高度
	private final int objectW; //宽度
	private final int parentWidth; //父对象宽度
	private final FlingListener mFlingListener;
	private final Object dataObject;
	private final float halfWidth; //卡片宽度的一半
	private float BASE_ROTATION_DEGREES;

	private float aPosX; //
	private float aPosY; //
	private float aDownTouchX; //手指按下时的x坐标
	private float aDownTouchY; //手指按下时的y坐标

	private boolean isMoved = false; //是否有拖动

	//
	private int mActivePointerId = INVALID_POINTER_ID;
	private View frame = null;

	private final int TOUCH_ABOVE = 0; //按得上半部分
	private final int TOUCH_BELOW = 1; //下半部分
	private int touchPosition;
	private boolean isAnimationRunning = false;
	private float MAX_COS = (float) Math.cos(Math.toRadians(45));

	public FlingCardListener(View frame, Object itemAtPosition, FlingListener flingListener)
	{
		this(frame, itemAtPosition, 15f, flingListener);
	}

	public FlingCardListener(View frame, Object itemAtPosition, float rotation_degrees, FlingListener flingListener)
	{
		super();
		this.frame = frame;
		this.objectX = frame.getX();
		this.objectY = frame.getY();
		this.objectH = frame.getHeight();
		this.objectW = frame.getWidth();
		this.halfWidth = objectW / 2f;
		this.dataObject = itemAtPosition;
		this.parentWidth = ((ViewGroup) frame.getParent()).getWidth();
		this.BASE_ROTATION_DEGREES = rotation_degrees;
		this.mFlingListener = flingListener;

	}

	@Override
	public boolean onTouch(View view, MotionEvent event)
	{
//		mActivePointerId = event.getPointerId(0);

		switch (event.getAction() & MotionEvent.ACTION_MASK)
		{
		case MotionEvent.ACTION_DOWN:
		{
			// from http://android-developers.blogspot.com/2010/06/making-sense-of-multitouch.html
			//记录按下的id
			mActivePointerId = event.getPointerId(0);
			float x = 0;
			float y = 0;
			boolean success = false;
			try
			{
				x = event.getX(mActivePointerId);
				y = event.getY(mActivePointerId);
				success = true;
			} catch (IllegalArgumentException e)
			{
				Log.w(TAG, "Exception in onTouch(view, event) : " + mActivePointerId, e);
			}
			if (success)
			{
				// 记录我们按下的点
				aDownTouchX = x;
				aDownTouchY = y;
				//to prevent an initial jump of the magnifier, aPosX and aPosY must
				//have the values from the magnifier frame
				if (aPosX == 0)
				{
					aPosX = frame.getX();
				}
				if (aPosY == 0)
				{
					aPosY = frame.getY();
				}

				if (y < objectH / 2)
				{
					touchPosition = TOUCH_ABOVE;
				} else
				{
					touchPosition = TOUCH_BELOW;
				}
			}

			//阻止触摸事件分发
			view.getParent().requestDisallowInterceptTouchEvent(false);
			break;
		}
		case MotionEvent.ACTION_UP:
		{
			mActivePointerId = INVALID_POINTER_ID;
			//重置堆栈，并且判断是否是单机效果决定时间的分发
			resetCardViewOnStack();

			//如果手指短暂的点击了卡片，则返回onClick；
			if (!isMoved)
			{
				mFlingListener.onClick(dataObject);
			}

			view.getParent().requestDisallowInterceptTouchEvent(false);
			break;
		}
		case MotionEvent.ACTION_POINTER_DOWN:
		{
			isMoved = false;
			break;
		}

		case MotionEvent.ACTION_POINTER_UP:
		{
			// Extract the index of the pointer that left the touch sensor
			final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			final int pointerId = event.getPointerId(pointerIndex);
			if (pointerId == mActivePointerId)
			{
				// This was our active pointer going up. Choose a new
				// active pointer and adjust accordingly.
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mActivePointerId = event.getPointerId(newPointerIndex);
			}
			break;
		}
		case MotionEvent.ACTION_MOVE:
		{
			// Find the index of the active pointer and fetch its position
			final int pointerIndexMove = event.findPointerIndex(mActivePointerId);
			final float xMove = event.getX(pointerIndexMove);
			final float yMove = event.getY(pointerIndexMove);

			//from http://android-developers.blogspot.com/2010/06/making-sense-of-multitouch.html
			// Calculate the distance moved
			final float dx = xMove - aDownTouchX;
			final float dy = yMove - aDownTouchY;

			// Move the frame
			aPosX += dx;
			aPosY += dy;

			// calculate the rotation degrees
			float distObjectX = aPosX - objectX;
			float rotation = BASE_ROTATION_DEGREES * 2.f * distObjectX / parentWidth;
			if (touchPosition == TOUCH_BELOW)
			{
				rotation = -rotation;
			}

			//in this area would be code for doing something with the view as the frame moves.
			frame.setX(aPosX);
			frame.setY(aPosY);
			frame.setRotation(rotation);
			mFlingListener.onMoveXY(aPosX, aPosY);
			//移动的距离超过4才算滚动
			if (Math.abs(distObjectX) > 4)
			{
				mFlingListener.onScroll(getScrollProgressPercent());
				isMoved = true;
			} else {
				isMoved = false;
			}
			break;
		}
		case MotionEvent.ACTION_CANCEL:
		{
			mActivePointerId = INVALID_POINTER_ID;
			view.getParent().requestDisallowInterceptTouchEvent(false);
			break;
		}
		}

		return true;
	}

	private float getScrollProgressPercent()
	{
		if (movedBeyondLeftBorder())
		{
			return -1f;
		} else if (movedBeyondRightBorder())
		{
			return 1f;
		} else
		{
			float zeroToOneValue = (aPosX + halfWidth - leftBorder()) / (rightBorder() - leftBorder());
			return zeroToOneValue * 2f - 1f;
		}
	}

	/**
	 * 重置卡片堆栈
	 *
	 * @return
	 */
	private boolean resetCardViewOnStack()
	{
		if (movedBeyondLeftBorder())
		{
			//左滑
			onSelected(true, getExitPoint(-objectW), 100);
			mFlingListener.onScroll(-1.0f);
			mFlingListener.onMoveXY(0, 0);
		} else if (movedBeyondRightBorder())
		{
			// 右滑
			onSelected(false, getExitPoint(parentWidth), 200);
			mFlingListener.onScroll(1.0f);
			mFlingListener.onMoveXY(0, 0);
		} else
		{
			mFlingListener.onMoveXY(0, 0);
			float absMoveDistance = Math.abs(aPosX - objectX);
			//距离不够回到起点
			aPosX = 0;
			aPosY = 0;
			aDownTouchX = 0;
			aDownTouchY = 0;

			frame.animate().setDuration(200).setInterpolator(new OvershootInterpolator(1.5f)).setListener(new AnimatorListenerAdapter()
			{
				@Override
				public void onAnimationRepeat(Animator animation)
				{
					super.onAnimationRepeat(animation);
				}
			}).x(objectX).y(objectY).rotation(0);
			mFlingListener.onScroll(0.0f);
		}
		return true;
	}

	private boolean movedBeyondLeftBorder()
	{
		return aPosX + halfWidth < leftBorder();
	}

	private boolean movedBeyondRightBorder()
	{
		return aPosX + halfWidth > rightBorder();
	}

	public float leftBorder()
	{
		return parentWidth / 5.f;
	}

	public float rightBorder()
	{
		return 3 * parentWidth / 5.f;
	}

	/**
	 * 卡片滑动
	 *
	 * @param isLeft   是否是左滑
	 * @param exitY    离开的y坐标
	 * @param duration 动画时长
	 */
	public void onSelected(final boolean isLeft, float exitY, long duration)
	{

		isAnimationRunning = true;
		float exitX;
		if (isLeft)
		{
			exitX = -objectW - getRotationWidthOffset();
		} else
		{
			exitX = parentWidth + getRotationWidthOffset();
		}

		this.frame.animate().setDuration(duration).setInterpolator(new AccelerateInterpolator()).x(exitX).y(exitY).setListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				if (isLeft)
				{
					mFlingListener.onCardExited();
					mFlingListener.leftExit(dataObject);
				} else
				{
					mFlingListener.onCardExited();
					mFlingListener.rightExit(dataObject);
				}
				isAnimationRunning = false;
			}
		}).rotation(getExitRotation(isLeft));
	}

	/**
	 * Starts a default left exit animation.
	 */
	public void selectLeft()
	{
		if (!isAnimationRunning)
			onSelected(true, objectY, 200);
	}

	/**
	 * Starts a default right exit animation.
	 */
	public void selectRight()
	{
		if (!isAnimationRunning)
			onSelected(false, objectY, 200);
	}

	/**
	 * 获取手指离开的坐标
	 *
	 * @param exitXPoint 离开的坐标
	 * @return
	 */
	private float getExitPoint(int exitXPoint)
	{
		float[] x = new float[2];
		x[0] = objectX;
		x[1] = aPosX;

		float[] y = new float[2];
		y[0] = objectY;
		y[1] = aPosY;

		LinearRegression regression = new LinearRegression(x, y);

		//Your typical y = ax+b linear regression
		return (float) regression.slope() * exitXPoint + (float) regression.intercept();
	}

	private float getExitRotation(boolean isLeft)
	{
		float rotation = BASE_ROTATION_DEGREES * 2.f * (parentWidth - objectX) / parentWidth;
		if (touchPosition == TOUCH_BELOW)
		{
			rotation = -rotation;
		}
		if (isLeft)
		{
			rotation = -rotation;
		}
		return rotation;
	}

	/**
	 * When the object rotates it's width becomes bigger.
	 * The maximum width is at 45 degrees.
	 * <p>
	 * The below method calculates the width offset of the rotation.
	 */
	private float getRotationWidthOffset()
	{
		return objectW / MAX_COS - objectW;
	}

	public void setRotationDegrees(float degrees)
	{
		this.BASE_ROTATION_DEGREES = degrees;
	}

	public boolean isTouching()
	{
		return this.mActivePointerId != INVALID_POINTER_ID;
	}

	public PointF getLastPoint()
	{
		return new PointF(this.aPosX, this.aPosY);
	}

	protected interface FlingListener
	{
		void onCardExited();

		void leftExit(Object dataObject);

		void rightExit(Object dataObject);

		void onClick(Object dataObject);

		void onScroll(float scrollProgressPercent);

		void onMoveXY(float moveX, float moveY);
	}

}
