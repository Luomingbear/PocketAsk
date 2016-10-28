package com.bear.pocketask.view.cardview;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

/**
 * 卡片自动生成的view
 * Created by luoming on 10/12/2016.
 */
public class CardSlideAdapterView extends AdapterView implements CardItemView.onCardItemSlidingListener, CardItemView.SpringUpdateListener
{
	private final String TAG = "CardSlideAdapterView";
	private Adapter mAdapter; //适配器
	private AdapterDataSetObserver mDataSetObserver;
	private boolean isLayout = false; //是否正在布局

	private View mTopCard; //顶部的卡片
	private final static int MAX_SHOW_NUM = 3; //卡片的最大显示数
	private final static int MAX_CARD_NUM = 4; //卡片的最大堆栈数量
	private final int MIN_ITEM_HEIGHT = 0; //卡片最小的偏移高度
	private int TOP_CARD_INDEX = 0; //最顶层的卡片的index

	private float mTouchX, mTouchY; //手指按下时的坐标
	private float mDistanceX, mDistanceY; //手指滑动的距离
	private boolean isAnimation = false;

	//
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
	//

	/**
	 * 初始化
	 *
	 * @param context     必须实现OnCardSlidingListener接口
	 * @param cardAdapter CardAdapter 数据适配器
	 */
	public void init(Context context, Adapter cardAdapter)
	{
		if (context instanceof OnCardSlidingListener)
		{
			mCardSlidingListener = (OnCardSlidingListener) context;
		} else
			throw new RuntimeException("Activity does not implement CardSlideAdapterView.OnCardSlidingListener");

		setAdapter(cardAdapter);
	}

	/**
	 * 设置适配器
	 *
	 * @param adapter
	 */
	@Override
	public void setAdapter(Adapter adapter)
	{
		if (mAdapter != null && mDataSetObserver != null)
		{
			mAdapter.unregisterDataSetObserver(mDataSetObserver);
			mDataSetObserver = null;
		}
		mAdapter = adapter;

		if (mAdapter != null && mDataSetObserver == null)
		{
			mDataSetObserver = new AdapterDataSetObserver();
			mAdapter.registerDataSetObserver(mDataSetObserver);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		//        Log.i(TAG, "onLayout: ++++++++");
		super.onLayout(changed, left, top, right, bottom);
		//如果adapter为空，则不需要执行后面的操作
		if (mAdapter == null)
			return;

		//开始布局
		isLayout = true;
		int viewCount = mAdapter.getCount();

		//如果卡片适配器的list里面对象的数量为零
		//则移除所有的view
		//否则添加卡片
		if (viewCount == 0)
			removeAllViewsInLayout();
		else
		{
			//如果添加过卡片则先移除以前的卡片再添加新的卡片
			if (mTopCard != null)
			{
				removeAllViewsInLayout();
				TOP_CARD_INDEX = 0;
				layoutCards(TOP_CARD_INDEX, viewCount);

			} else
				layoutCards(TOP_CARD_INDEX, 1);
			TOP_CARD_INDEX++;
		}

		//布局完成
		isLayout = false;
	}

	private void layoutCards(int index, int adapterCount)
	{
		while (index < Math.min(adapterCount, MAX_CARD_NUM))
		{
			addAndMeasureChild(index);
			index++;
		}
	}

	/**
	 * 添加并且计算卡片的宽高
	 *
	 * @param index
	 */
	private void addAndMeasureChild(int index)
	{
		//addView
		View child = mAdapter.getView(index, null, this);
		LayoutParams params = child.getLayoutParams();
		if (params == null)
			params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		addViewInLayout(child, 0, params, true);

		//measureView
		boolean isNeedMeasure = child.isLayoutRequested();
		if (isNeedMeasure)
		{
			int itemWidth = getWidth();
			child.measure(MeasureSpec.EXACTLY | itemWidth, MeasureSpec.UNSPECIFIED);
		}

		//设置顶部的卡片
		mTopCard = child;

		//设置卡片的位置
		positionCards(child, index);

		//设置监听器
		((CardItemView) child).setOnCardItemSlideListener(this);
		((CardItemView) child).setSpringUpdateListener(this);

		//设置id
		if (child.getId() == View.NO_ID)
			child.setId(child.hashCode());
	}

	/**
	 * 设置卡片显示的位置
	 */
	private void positionCards(View child, int index)
	{
		if (child == null)
			return;

		index = Math.min(index, MAX_SHOW_NUM);

		int width = child.getMeasuredWidth();
		int height = child.getMeasuredHeight();

		int left = (getWidth() - width) / 2;
		int top = (getHeight() - height) / 2;
		child.layout(left, top, left + width, top + height);

		adjustChildView(child, index);
	}

	/**
	 * 滑动卡片的时候使后面的卡片能够进行微微的动画
	 */
	private void animationCards()
	{
		//getChild 的下标与addChild的下标顺序是相反的！！！
		int num = Math.min(MAX_CARD_NUM, mAdapter.getCount()) - 1;
		for (int i = num - 1; i > TOP_CARD_INDEX - 1; i--)
		{

			View card = getChildAt(i);
			if (card == null)
				continue;
			//
			float width = card.getWidth();

			if (width == 0)
				continue;
			float disX = Math.min(Math.abs(mDistanceX), width / 4);

			float scale = (disX / width * 4) * ((num - i) / 20.0f);

			scale = (1 - (num - i) / 20.0f) + scale;
			//            Log.i(TAG, "animationCards: 第" + i + "个卡片的scale是" + scale);

			scale = Math.min(scale, 1);
			card.setScaleX(scale);
			card.setScaleY(scale);
		}
	}

	/**
	 * 调节卡片的大小和位置
	 *
	 * @param child
	 * @param index
	 */
	private void adjustChildView(View child, int index)
	{
		if (child == null)
			return;

		if (index > MAX_SHOW_NUM - 1)
			index = MAX_SHOW_NUM - 1;
		//缩放
		child.setScaleX(1 - index / 20.0f);
		child.setScaleY(1 - index / 20.0f);
		//偏移
		child.offsetTopAndBottom(index * MIN_ITEM_HEIGHT);

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		switch (ev.getAction() & ev.getActionMasked())
		{
		case MotionEvent.ACTION_DOWN:
			mTouchX = ev.getX();
			mTouchY = ev.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			mDistanceX = ev.getX() - mTouchX;
			mDistanceY = ev.getY() - mTouchY;

			animationCards();
			break;

		case MotionEvent.ACTION_UP:

			break;

		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public void onUpdateX(float posX)
	{
		mDistanceX = posX;
		isAnimation = true;
		//        animationCards();
		//        positionCards();
	}

	@Override
	public void onUpdateY(float posY)
	{
		//        mDistanceY = posY;
		//        if (!isAnimation)
		//            animationCards();
	}

	@Override
	public View getSelectedView()
	{
		return mTopCard;
	}

	@Override
	public void setSelection(int position)
	{

	}

	@Override
	public Adapter getAdapter()
	{
		return mAdapter;
	}

	@Override
	public void requestLayout()
	{
		if (!isLayout)
			super.requestLayout();
	}

	@Override
	public void onExitLeft()
	{
		mCardSlidingListener.onExitLeft();

	}

	@Override
	public void onExitRight()
	{
		mCardSlidingListener.onExitRight();

	}

	@Override
	public void onRemove()
	{
		if (getChildCount() > 0) {
			removeViewsInLayout(0, 1);
			mCardSlidingListener.onRemove();
		}

	}

	/**
	 * 点击卡片
	 */
	@Override
	public void onClicked()
	{
		mCardSlidingListener.onClicked();
	}

	/**
	 * 数据更新观察者
	 */
	private class AdapterDataSetObserver extends DataSetObserver
	{
		@Override
		public void onChanged()
		{
			requestLayout();
		}

		@Override
		public void onInvalidated()
		{
			requestLayout();
		}

	}

	private OnCardSlidingListener mCardSlidingListener;

	/**
	 * 卡片的拖动回调函数
	 */
	public interface OnCardSlidingListener
	{
		//当卡片被移除的时候执行
		void onRemove();

		//点击了卡片的非特殊区域时执行
		void onClicked();

		//当卡片向左移除时执行
		void onExitLeft();

		//当卡片向右移除时执行
		void onExitRight();
	}
}