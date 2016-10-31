package com.bear.pocketask.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bear on 16/10/30.
 */

public class MoveView extends View {

    private float mCenterX; //圆心的坐标

    private Paint mPaint; //画笔

    public MoveView(Context context) {
        this(context, null);
    }

    public MoveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint(); //初始化画笔
        mCenterX = -50;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);

        canvas.drawCircle(mCenterX, getHeight() / 2, 50, mPaint);

        move();
    }

    /**
     * 通过改变圆心的坐标使圆移动
     */
    private void move() {
        //圆心x坐标每次加二
        if (mCenterX < getWidth())
            mCenterX += 2;
        else mCenterX = -50;

        //刷新view
        invalidate();
    }
}
