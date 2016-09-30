package com.bear.pocketask.view;

import java.util.Timer;
import java.util.TimerTask;

import com.bear.pocketask.R;
import com.bear.pocketask.utils.DipPxConversion;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 录音view
 * 类似微信的语音播放按钮
 * 执行setRecordViewListener（）方法监听点击事件，返回当前是否处于播放状态
 * Created by luoming on 9/30/2016.
 */
public class RecordView extends View {
    private Paint mPaint; //画笔
    private float mRoundSize; //圆角值
    private float mStrokeWidth; //画笔的宽度
    private float mWidth; //view宽度
    private float mHeight; //view高度

    private int mBackgroundColor;//背景色
    private int mRecordedColor; //没有播放的波纹的颜色
    private int mRecordingColor; //正在播放的波纹的颜色

    private boolean isPlay = false; //是否正在播放
    private int mRecordNum = 3; //播放高亮的波纹数
    private Timer timer; //定时器

    public RecordView(Context context) {
        this(context, null);
    }

    public RecordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordView);
        mRoundSize = typedArray.getDimension(R.styleable.RecordView_roundSize, 0);
        mBackgroundColor = typedArray.getColor(R.styleable.RecordView_backgroundColor, Color.GRAY);
        mStrokeWidth = typedArray.getDimension(R.styleable.RecordView_strokeWidth, DipPxConversion.dip2px(context, 2));
        typedArray.recycle();

        mRecordingColor = Color.WHITE;
        mRecordedColor = getResources().getColor(R.color.lightgray);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setAntiAlias(true);

        mWidth = getWidth();
        mHeight = getHeight();
        //绘制背景
        drawBackground(canvas);
        //绘制波纹
        float xCenter = mWidth / 2 - mHeight / 4;
        //绘制背景浅色波纹
        drawRecordArc(canvas, xCenter, mRecordedColor, 3);
        //
        drawRecordArc(canvas, xCenter, mRecordingColor, mRecordNum);
    }

    /**
     * 绘制圆角背景
     *
     * @param canvas 画布
     */
    private void drawBackground(Canvas canvas) {
        RectF rectF = new RectF(0, 0, mWidth, mHeight);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackgroundColor);
        canvas.drawRoundRect(rectF, mRoundSize, mRoundSize, mPaint);
    }

    /**
     * 绘制一条波纹
     *
     * @param canvas  画布
     * @param xCenter 波纹的圆心x轴
     * @param radius  波纹的半径
     */
    private void drawArc(Canvas canvas, float xCenter, float radius, int color) {
        RectF oval = new RectF(xCenter - radius, mHeight / 2 - radius, xCenter + radius, mHeight / 2 + radius);

        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(color);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, -30, 60, false, mPaint);
    }

    /**
     * 绘制组合波纹
     *
     * @param canvas
     * @param xCenter 波纹显示的中心
     * @param color   波纹的颜色
     * @param arcNum  波纹的条数
     */
    private void drawRecordArc(Canvas canvas, float xCenter, int color, int arcNum) {
        for (int i = 1; i <= arcNum; i++) {
            drawArc(canvas, xCenter, mHeight / 2 / 4 * i, color);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP: {
                setPlay();
                break;
            }
        }
        return true;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: //播放
                {
                    if (mRecordNum < 3)
                        mRecordNum++;
                    else mRecordNum = 1;
                    invalidate();
                    break;
                }
            }
        }
    };

    public boolean isPlay() {
        return isPlay;
    }

    /**
     * 设置播放或者停止
     *
     * @param play
     */
    public void setPlay(boolean play) {
        isPlay = play;
        //监听点击事件
        if (recordViewListener != null)
            recordViewListener.onClick(isPlay);
        //
        if (isPlay) {
            timer = new Timer();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    //由于主线程安全，页面的更新需放到主线程中
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            };
            timer.schedule(task, 300, 300);
        } else {
            //取消定时器
            timer.cancel();
            //恢复到波纹全部高亮显示
            mRecordNum = 3;
            invalidate();
        }
    }

    public void setPlay() {
        setPlay(!isPlay);
    }

    private RecordViewListener recordViewListener;

    public void setRecordViewListener(RecordViewListener recordViewListener) {
        this.recordViewListener = recordViewListener;
    }

    /**
     * 点击事件
     * 返回按钮的状态，是否处于播放状态
     */
    public interface RecordViewListener {
        void onClick(boolean isPlay);
    }
}
