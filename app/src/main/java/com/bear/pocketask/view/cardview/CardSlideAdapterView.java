package com.bear.pocketask.view.cardview;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

/**
 * 卡片自动生成的view
 * Created by luoming on 10/12/2016.
 */
public class CardSlideAdapterView extends AdapterView implements CardItemView.onCardItemSlidingListener, CardItemView.SpringUpdateListener {
    private static final String TAG = "CardSlideAdapterView";
    private Adapter mAdapter; //适配器
    private AdapterDataSetObserver mDataSetObserver;

    private final static int MAX_SHOW_NUM = 3; //卡片的最大显示数
    private final static int MAX_CARD_NUM = 4; //卡片的最大堆栈数量
    private int TOP_CARD_INDEX = 0; //最顶层的卡片的index

    private float mTouchX, mTouchY; //手指按下时的坐标
    private float mDistanceX, mDistanceY; //手指滑动的距离
    private float topPosX, topPosY; ////最顶层的卡片的位置

    public CardSlideAdapterView(Context context) {
        this(context, null);
    }

    public CardSlideAdapterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardSlideAdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 初始化
     *
     * @param context     必须实现OnCardSlidingListener接口
     * @param cardAdapter CardAdapter 数据适配器
     */
    public void init(Context context, Adapter cardAdapter) {
        if (context instanceof OnCardSlidingListener) {
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
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
            mDataSetObserver = null;
        }
        mAdapter = adapter;

        if (mAdapter != null && mDataSetObserver == null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mAdapter == null)
            return;
        int viewCount = mAdapter.getCount();
        if (viewCount == 0) {
            removeAllViewsInLayout();
        } else {
            if (TOP_CARD_INDEX > MAX_CARD_NUM)
                TOP_CARD_INDEX = 0;
            Log.i(TAG, "onLayout: TOP_INDEX++" + TOP_CARD_INDEX);
            View card = mAdapter.getView(TOP_CARD_INDEX, null, this);
            addAndMeasureChild(card);
            ((CardItemView) card).setOnCardItemSlideListener(this);
            ((CardItemView) card).setSpringUpdateListener(this);

            TOP_CARD_INDEX++;

        }
        positionCards();
    }

    /**
     * 添加并且计算卡片的宽高
     *
     * @param child
     */
    private void addAndMeasureChild(View child) {
        LayoutParams params = child.getLayoutParams();
        if (params == null)
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addViewInLayout(child, 0, params, true);

        boolean isNeedMeasure = child.isLayoutRequested();
        if (isNeedMeasure) {
            int itemWidth = getWidth();
            child.measure(MeasureSpec.EXACTLY | itemWidth, MeasureSpec.UNSPECIFIED);
        }
    }

    /**
     * 设置卡片显示的位置
     */
    private void positionCards() {
        int i;
        for (i = 0; i < MAX_CARD_NUM; i++) {
            View child = getChildAt(i);
            if (child != null) {
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();

                int left = (getWidth() - width) / 2;
                int top = (getHeight() - height) / 2;
                child.layout(left, top, left + width, top + height);
            }

//            adjustChildView(child, i);
        }
    }

    /**
     * 调节卡片的大小和位置
     *
     * @param child
     * @param index
     */
    private void adjustChildView(View child, int index) {
        if (child == null)
            return;

        if (index > 1)
            index = 2;

        child.setScaleX(1 - (MAX_CARD_NUM - index) / 10.0f);
        child.setScaleY(1 - (MAX_CARD_NUM - index) / 10.0f);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction() & ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                mTouchX = ev.getX();
                mTouchY = ev.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                mDistanceX = ev.getX() - mTouchX;
                mDistanceY = ev.getY() - mTouchY;
                //                positionCards();
                Log.i(TAG, "onInterceptTouchEvent: disX++" + mDistanceX);
                break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void onUpdateX(float posX) {
        mDistanceX = posX;
        //        positionCards();
    }

    @Override
    public void onUpdateY(float posY) {

    }

    @Override
    public View getSelectedView() {
        return null;
    }

    @Override
    public void setSelection(int position) {

    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onExitLeft() {

    }

    @Override
    public void onExitRight() {

    }

    @Override
    public void onRemove() {
        //		Toast.makeText(getContext(), "移除", Toast.LENGTH_SHORT).show();
        mCardSlidingListener.onRemove();

    }

    /**
     * 点击卡片
     */
    @Override
    public void onClicked() {
        mCardSlidingListener.onClicked();
    }

    /**
     * 数据更新观察者
     */
    private class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            requestLayout();
        }

    }

    private OnCardSlidingListener mCardSlidingListener;

    public interface OnCardSlidingListener {
        void onRemove();

        void onClicked();
    }
}
