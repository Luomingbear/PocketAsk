package com.bear.pocketask.view.cardview;

import com.bear.pocketask.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.FrameLayout;

/**
 * 卡片集合
 * Created by bear on 16/10/7.
 */

public class SwipeFlingAdapterView extends BaseFlingAdapterView implements CardItemView.onCardSlidingListener {
    private int MAX_VISIBLE = 2; //最多可见的卡片数量
    private int MIN_ADAPTER_STACK = 4;
    private float ROTATION_DEGREES = 15.f; //旋转的角度
    private float ITEM_SMALL_WIDTH = 20; //卡片的最小宽度
    private float ITEM_SMALL_HIGH = 32; //卡片最小高度

    private Adapter mAdapter; //适配器
    private int LAST_OBJECT_IN_STACK = 0;
    private onFlingListener mFlingListener;
    private AdapterDataSetObserver mDataSetObserver;
    private boolean mInLayout = false;
    private CardItemView mActiveCard = null;
    private OnItemClickListener mOnItemClickListener;
    private PointF mLastTouchPoint;

    private float p = 0f;

    public SwipeFlingAdapterView(Context context) {
        this(context, null);
    }

    public SwipeFlingAdapterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeFlingAdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeFlingAdapterView, defStyle, 0);
        MAX_VISIBLE = a.getInt(R.styleable.SwipeFlingAdapterView_max_visible, MAX_VISIBLE);
        MIN_ADAPTER_STACK = a.getInt(R.styleable.SwipeFlingAdapterView_min_adapter_stack, MIN_ADAPTER_STACK);
        ROTATION_DEGREES = a.getFloat(R.styleable.SwipeFlingAdapterView_rotation_degrees, ROTATION_DEGREES);
        a.recycle();
    }

    private float dpToPx(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sp, getContext().getResources().getDisplayMetrics());
    }

    /**
     * A shortcut method to set both the listeners and the adapter.
     *
     * @param context  The activity context which extends onFlingListener, OnItemClickListener or both
     * @param mAdapter The adapter you have to set.
     */
    public void init(final Context context, Adapter mAdapter) {
        if (context instanceof onFlingListener) {
            mFlingListener = (onFlingListener) context;
        } else {
            throw new RuntimeException("Activity does not implement SwipeFlingAdapterView.onFlingListener");
        }
        if (context instanceof OnItemClickListener) {
            mOnItemClickListener = (OnItemClickListener) context;
        }
        setAdapter(mAdapter);
    }

    @Override
    public View getSelectedView() {
        return mActiveCard;
    }

    @Override
    public void requestLayout() {
        if (!mInLayout) {
            super.requestLayout();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // if we don't have an adapter, we don't need to do anything
        if (mAdapter == null) {
            return;
        }

        mInLayout = true;
        final int adapterCount = mAdapter.getCount();

        if (adapterCount == 0) {
            removeAllViewsInLayout();
        } else {
            View topCard = getChildAt(LAST_OBJECT_IN_STACK);
            if (mActiveCard != null && topCard != null && topCard == mActiveCard) {
                //				if (this.flingCardListener.isTouching())
                //				{
                //					PointF lastPoint = this.flingCardListener.getLastPoint();
                //					if (this.mLastTouchPoint == null || !this.mLastTouchPoint.equals(lastPoint))
                //					{
                //						this.mLastTouchPoint = lastPoint;
                //						removeViewsInLayout(0, LAST_OBJECT_IN_STACK);
                //						layoutChildren(1, adapterCount, 3);
                //					}
                //				}
            } else {
                // Reset the UI and set top view listener
                removeAllViewsInLayout();
                layoutChildren(0, adapterCount);
                setTopView();
            }
        }

        mInLayout = false;

        if (adapterCount <= MIN_ADAPTER_STACK)
            mFlingListener.onAdapterAboutToEmpty(adapterCount);
    }

    private void layoutChildren(int startingIndex, int adapterCount) {

        while (startingIndex < Math.min(adapterCount, 4)) {
            View newUnderChild = mAdapter.getView(startingIndex, null, this);
            if (newUnderChild.getVisibility() != GONE) {
                makeAndAddView(startingIndex, newUnderChild);
                LAST_OBJECT_IN_STACK = startingIndex;
            }
            startingIndex++;
        }
    }


    private static final String TAG = "SwipeFlingAdapterView";
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: Adapter");
        return super.onTouchEvent(event);
    }

    /**
     * 跳转改变view 大小
     *
     * @param child
     * @param index
     */
    private void adjustChildView(View child, int index) {
        int n;
        if (index > 1)
            n = 2;
        else
            n = index;
        //p移动的比例 ?/500
        if (index == 3 && p > 0.5f) {
            n = index;
        }
        //位移越来越小
        //		int offset = (int) (dpToPx((int) ITEM_SMALL_HIGH) * n * (1 - 0.1f * (n - p)));
        //		child.offsetTopAndBottom(offset);
        child.setScaleX(1 - 0.1f * (n - p));
        child.setScaleY(1 - 0.1f * (n - p));
    }

    /**
     * 绘制子View
     *
     * @param index
     * @param child
     */
    private void makeAndAddView(int index, View child) {

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        addViewInLayout(child, 0, lp, true);
        ((CardItemView) child).setOnCardSlideListener(this);

        final boolean needToMeasure = child.isLayoutRequested();
        if (needToMeasure) {
            int childWidthSpec = getChildMeasureSpec(getWidthMeasureSpec(), getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
            int childHeightSpec = getChildMeasureSpec(getHeightMeasureSpec(), getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin, lp.height);
            child.measure(childWidthSpec, childHeightSpec);
        } else {
            cleanupLayoutState(child);
        }

        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int gravity = lp.gravity;
        if (gravity == -1) {
            //设置卡片的位置 居中
            gravity = Gravity.CENTER | Gravity.START;
        }

        final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

        int childLeft;
        int childTop;
        switch (Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.CENTER_HORIZONTAL:
                childLeft = (getWidth() + getPaddingLeft() - getPaddingRight() - w) / 2 + lp.leftMargin - lp.rightMargin;
                break;
            case Gravity.END:
                childLeft = getWidth() + getPaddingRight() - w - lp.rightMargin;
                break;
            case Gravity.START:
            default:
                int l = 0;
                childLeft = getPaddingLeft() + lp.leftMargin + l;
                break;
        }
        switch (verticalGravity) {
            case Gravity.CENTER_VERTICAL:
                childTop = (getHeight() + getPaddingTop() - getPaddingBottom() - h) / 2 + lp.topMargin - lp.bottomMargin;
                break;
            case Gravity.BOTTOM:
                childTop = getHeight() - getPaddingBottom() - h - lp.bottomMargin;
                break;
            case Gravity.TOP:
            default:
                int top = 0;
                childTop = getPaddingTop() + lp.topMargin + top;
                break;
        }

        child.layout(childLeft, childTop, childLeft + w, childTop + h);
        adjustChildView(child, index);
    }

    /**
     * Set the top view and add the fling listener
     */
    private void setTopView() {
        if (getChildCount() > 0) {

            mActiveCard = (CardItemView) getChildAt(LAST_OBJECT_IN_STACK);

        }
    }

    public void setMaxVisible(int MAX_VISIBLE) {
        this.MAX_VISIBLE = MAX_VISIBLE;
    }

    public void setMinStackInAdapter(int MIN_ADAPTER_STACK) {
        this.MIN_ADAPTER_STACK = MIN_ADAPTER_STACK;
    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

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

    public void setFlingListener(onFlingListener onFlingListener) {
        this.mFlingListener = onFlingListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FrameLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    public void onExitLeft() {

    }

    @Override
    public void onExitRight() {

    }

    @Override
    public void onRemove() {

    }

    @Override
    public void onClicked() {
        mOnItemClickListener.onItemClicked(0, mActiveCard);
    }

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

    public interface OnItemClickListener {
        void onItemClicked(int itemPosition, Object dataObject);
    }

    public interface onFlingListener {
        void removeFirstObjectInAdapter();

        void onLeftCardExit(Object dataObject);

        void onRightCardExit(Object dataObject);

        void onAdapterAboutToEmpty(int itemsInAdapter);

        void onScroll(float scrollProgressPercent);

    }
}
