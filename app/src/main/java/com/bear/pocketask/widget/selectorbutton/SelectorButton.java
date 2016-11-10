package com.bear.pocketask.widget.selectorbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bear.pocketask.R;
import com.bear.pocketask.utils.DipPxConversion;

/**
 * 单选或者多选按钮
 * 通过执行setChecked切换选中与未选中的状态
 * Created by bear on 16/10/15.
 */

public class SelectorButton extends View {

    private static final String TAG = "SelectButton";
    private Paint mPaint; //画笔

    private int mCheckedColor; //点击后显示的颜色
    private int mUnCheckColor; //未点击显示的颜色

    private int mBackgroundColor; //背景颜色
    private boolean isChecked = false; //是否选中啦
    private float centerX = 0, centerY = 0; //屏幕的中心

    private IconPosition mIconPosition; //图标的位置
    private float mIconCenterX, mIconCenterY; //图标的位置
    private float mIconRadius; //图标的半径
    private int mIconColor; //图标的颜色

    private float mTextSize; //文本大小
    private int mTextColor; //文本颜色
    private String mTextString; //文本字符串

    private float mIconPadding; //图标与文本的距离
    private float mLayoutPadding; //图标与边界的距离

    private boolean isClicking = false; //是否正在点击

    public SelectorButton(Context context) {
        this(context, null);
    }

    public SelectorButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectorButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectorButton);
        mTextColor = typedArray.getColor(R.styleable.SelectorButton_text_color, getResources().getColor(R.color.deepblue));
        mTextSize = typedArray.getDimension(R.styleable.SelectorButton_text_size, getResources().getDimension(R.dimen.font_normal));
        mTextString = typedArray.getString(R.styleable.SelectorButton_text_string);
        mIconPadding = typedArray.getDimension(R.styleable.SelectorButton_icon_padding, getResources().getDimension(R.dimen.margin_small));
        mLayoutPadding = typedArray.getDimension(R.styleable.SelectorButton_layout_padding, getResources().getDimension(R.dimen.margin_normal));
        mIconPosition = IconPosition.values()[typedArray.getInt(R.styleable.SelectorButton_icon_position, 0)];
        typedArray.recycle();

        init();
    }

    private void init() {
        mPaint = new Paint();

        mIconRadius = DipPxConversion.dip2px(getContext(), 6);
        mIconColor = getResources().getColor(R.color.deepblue);
        mCheckedColor = getResources().getColor(R.color.grayblue);
        mUnCheckColor = Color.WHITE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        //设置中心点坐标
        if (centerX == 0 || centerY == 0) {
            centerX = getWidth() / 2;
            centerY = getHeight() / 2;
        }
        //设置背景色
        drawBg(canvas);
        //绘制图标
        initIconPosition(canvas);
        drawIcon(canvas);

        //绘制文本
        if (mTextString != null)
            drawText(canvas);
    }

    private void drawBg(Canvas canvas) {
        mBackgroundColor = isClicking ? mCheckedColor : Color.TRANSPARENT;
        mPaint.setColor(mBackgroundColor);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }

    /**
     * 绘制图标
     *
     * @param canvas
     */
    private void initIconPosition(Canvas canvas) {
        switch (mIconPosition) {
            case BOTTOM:
                mIconCenterX = centerX;
                mIconCenterY = getHeight() - mIconRadius - mLayoutPadding;
                break;
            case LEFT:
                mIconCenterX = mIconRadius + mLayoutPadding;
                mIconCenterY = centerY;
                break;
            case RIGHT:
                mIconCenterX = getWidth() - mIconRadius - mLayoutPadding;
                mIconCenterY = centerY;
                break;
            case TOP:
                mIconCenterX = centerX;
                mIconCenterY = mIconRadius + mLayoutPadding;
                break;
        }
    }

    /**
     * 绘制图标
     *
     * @param canvas
     */
    private void drawIcon(Canvas canvas) {
        //绘制图标背景
        mPaint.setColor(mIconColor);
        canvas.drawCircle(mIconCenterX, mIconCenterY, mIconRadius, mPaint);

        //绘制选中之后的形状
        if (isChecked) {
            //绘制渐变的圆形图标
            LinearGradient gradient = new LinearGradient(mIconCenterX - mIconRadius, mIconCenterY - mIconRadius, mIconCenterX + mIconRadius,
                    mIconCenterY + mIconRadius, mCheckedColor, Color.WHITE, Shader.TileMode.CLAMP);
            mPaint.setShader(gradient);

            canvas.drawCircle(mIconCenterX, mIconCenterY, mIconRadius * 0.6f, mPaint);
            mPaint.setShader(null);
        }

    }

    /**
     * 绘制文本
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);

        float textX = 0, textY = 0;
        float textWidth = mPaint.measureText(mTextString);

        switch (mIconPosition) {
            case BOTTOM:
                textX = centerX - textWidth / 2;
                textY = getHeight() - mIconRadius * 2 - mLayoutPadding - mIconPadding;
                break;
            case LEFT:
                textX = mIconRadius * 2 + mIconPadding + mLayoutPadding;
                textY = mIconCenterY + mTextSize / 3;
                break;
            case RIGHT:
                textX = getWidth() - (mIconRadius * 2 + mIconPadding) - mLayoutPadding - textWidth;
                textY = mIconCenterY + mTextSize / 3;
                break;
            case TOP:
                textX = centerX - textWidth / 2;
                textY = mIconRadius * 2 + mLayoutPadding + mIconPadding + 2 * mTextSize / 3;
                break;
        }

        canvas.drawText(mTextString, textX, textY, mPaint);

    }

    public void setChecked() {
        setChecked(!isChecked());
    }

    public void setChecked(boolean checked) {
        isChecked = checked;

        invalidate();
    }

    public boolean isChecked() {
        return isChecked;
    }

    public String getTextString() {
        return mTextString;
    }

    public void setTextString(String textString) {
        this.mTextString = textString;
        invalidate();
    }

    private float touchX, touchY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchY = event.getY();
                isClicking = true;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                isClicking = false;

                float move = Math.max(Math.abs(event.getX() - touchX), Math.abs(event.getY() - touchY));
                if (move < 4) {
                    setChecked(!isChecked());

                    if (selectorChangeListener != null)
                        selectorChangeListener.onChange(this, isChecked);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec == 0)
            widthMeasureSpec = getContext().getWallpaper().getIntrinsicWidth();
        if (heightMeasureSpec == 0)
            heightMeasureSpec = (int) getResources().getDimension(R.dimen.title_height);

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    private SelectorChangeListener selectorChangeListener;

    public void setSelectorChangeListener(SelectorChangeListener selectorChangeListener) {
        this.selectorChangeListener = selectorChangeListener;
    }

    public interface SelectorChangeListener {
        void onChange(View view, boolean isChecked);
    }

    public enum IconPosition {
        LEFT, TOP, RIGHT, BOTTOM
    }
}