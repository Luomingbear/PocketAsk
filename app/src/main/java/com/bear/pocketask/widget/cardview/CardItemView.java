package com.bear.pocketask.widget.cardview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

/**
 * 卡片布局
 * 自定义拦截touch事件
 * Created by luoming on 10/9/2016.
 */
public class CardItemView extends RelativeLayout {
    private static final String TAG = "CardItemView";
    private Spring mSpringX, mSpringY; //反弹
    private double mTension = 20; //拉力
    private double mFriction = 5; //摩擦力
    private float mTouchX, mTouchY; //手指按下时的坐标
    private float mDistanceX = 0, mDistanceY = 0; //手指移动的总距离
    private float mPosX = 0, mPosY = 0; //卡片的初始坐标
    private float mMaxRotation = 8; //限制卡片旋转的角度

    private float mCardHalfWidth = 0; //卡片的宽度的1/4

    private float oldSpringX; //上一次的springX的值

    private boolean isClicked = false;

    private boolean isRemove = true;//是否移除了
    private boolean isOutLeft = false;//向左滑出

    public CardItemView(Context context) {
        this(context, null);
    }

    public CardItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSpring();
    }

    public void initSpring() {
        SpringConfig config = SpringConfig.fromOrigamiTensionAndFriction(mTension, mFriction);
        SpringSystem system = SpringSystem.create();

        mSpringX = system.createSpring();
        mSpringX.setSpringConfig(config);
        mSpringX.addListener(new SpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float x = (float) spring.getCurrentValue();
                setX(x);
                float rotation = x * mMaxRotation / 360;
                setRotation(rotation);
                //将当前的位置传递给父类让他更新卡片堆栈的其他卡片的位置
                if (springUpdateListener != null)
                    springUpdateListener.onUpdateX(x - mPosX);

                //如果变化范围小于0.05则视为静止了，返回已经移除
                float d = (x - oldSpringX);
                oldSpringX = x;
                if (Math.abs(d) < 0.05)
                    if (onCardSlideListener != null && !isRemove) {
                        onCardSlideListener.onRemove();

                        if (isOutLeft)
                            onCardSlideListener.onExitLeft();
                        else
                            onCardSlideListener.onExitRight();
                        isRemove = true;
                    }

            }

            @Override
            public void onSpringAtRest(Spring spring) {
            }

            @Override
            public void onSpringActivate(Spring spring) {

            }

            @Override
            public void onSpringEndStateChange(Spring spring) {

            }
        });
        mSpringY = system.createSpring();
        mSpringY.setSpringConfig(config);
        mSpringY.addListener(new SpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float y = (float) spring.getCurrentValue();
                setY(y);
                if (springUpdateListener != null)
                    springUpdateListener.onUpdateY(y - mPosY);
            }

            @Override
            public void onSpringAtRest(Spring spring) {

            }

            @Override
            public void onSpringActivate(Spring spring) {

            }

            @Override
            public void onSpringEndStateChange(Spring spring) {

            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float distance;
        switch (ev.getAction() & ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mTouchX = ev.getX();
                mTouchY = ev.getY();
                Log.i(TAG, "onInterceptTouchEvent: 按下的x：" + mTouchX);
                //获取当前的卡片位置，因为onTouchEvent可能会收不到touchDown事件，所以在这里获取
                if (mPosX == 0 || mPosY == 0) {
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
                Log.i(TAG, "onInterceptTouchEvent: 滑动的距离：" + distance);
                if (distance > 5)
                    return true;
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                //移动值设置为初始位置
                mDistanceX = mPosX;
                mDistanceY = mPosY;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                //计算卡片的位置
                mDistanceX += event.getX() - mTouchX;
                mDistanceY += event.getY() - mTouchY;

                isRemove = true;
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
            case MotionEvent.ACTION_UP: {
                //设置滑动动画
                if (mDistanceX < mPosX - mCardHalfWidth)
                    setSlideLeft();
                else if (mDistanceX > mPosX + mCardHalfWidth)
                    setSlideRight();
                else
                    setSlideBack();

                //设置是否是点击效果
                isClicked = Math.max(Math.abs(mDistanceX - mPosX), Math.abs(mDistanceY - mPosY)) <= 5;

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
    public void setSlideBack() {
        mSpringX.setCurrentValue(mDistanceX);
        mSpringX.setEndValue(mPosX);
        mSpringY.setCurrentValue(mDistanceY);
        mSpringY.setEndValue(mPosY);
    }

    /**
     * 设置自动滑出左边界
     */
    public void setSlideLeft() {
        isOutLeft = true;
        isRemove = false;

        mSpringX.setCurrentValue(getX());
        mSpringX.setEndValue(-getWidth() * 1.5);

        mSpringY.setCurrentValue(getY());
        //设置离开的y坐标，根据当前手指的滑动计算
        double y;
        y = ((mDistanceY - mPosY) * (mPosX + getWidth()) / Math.abs(mDistanceX - mPosX));
        mSpringY.setEndValue(y);
    }

    /**
     * 自动滑出右边界
     */
    public void setSlideRight() {
        isRemove = false;
        isOutLeft = false;

        mSpringX.setCurrentValue(getX());
        mSpringX.setEndValue(getWidth() * 1.5);

        mSpringY.setCurrentValue(getY());
        //设置离开的y坐标，根据当前手指的滑动计算
        double y;
        y = ((mDistanceY - mPosY) * (mPosX + getWidth()) / Math.abs(mDistanceX - mPosX));
        mSpringY.setEndValue(y);

    }

    private onCardItemSlidingListener onCardSlideListener;

    public void setOnCardItemSlideListener(onCardItemSlidingListener onCardSlideListener) {
        this.onCardSlideListener = onCardSlideListener;
    }

    /**
     * 卡片滑动的回调
     */
    public interface onCardItemSlidingListener {
        //向左滑出
        void onExitLeft();

        //向右滑出
        void onExitRight();

        //移除
        void onRemove();

        //点击
        void onClicked();
    }

    private SpringUpdateListener springUpdateListener;

    public void setSpringUpdateListener(SpringUpdateListener springUpdateListener) {
        this.springUpdateListener = springUpdateListener;
    }

    /**
     * 弹簧更新回调
     */
    public interface SpringUpdateListener {
        void onUpdateX(float posX);

        void onUpdateY(float posY);
    }
}
