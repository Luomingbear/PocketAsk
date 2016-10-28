package com.bear.pocketask.view.record;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.bear.pocketask.R;
import com.bear.pocketask.utils.DipPxConversion;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 录音view
 * 类似微信的语音播放按钮
 * 执行setRecordViewListener（）方法监听点击事件，返回当前是否处于播放状态
 * Created by luoming on 9/30/2016.
 */
public class RecordView extends View implements RecordObservable.RecordObserver
{
	private Paint mPaint; //画笔
	private float mRoundSize; //圆角值
	private float mStrokeWidth; //画笔的宽度
	private float mWidth; //view宽度
	private float mHeight; //view高度

	private int mBackgroundColor;//背景色
	private int mRecordedColor; //没有播放的波纹的颜色
	private int mRecordingColor; //正在播放的波纹的颜色

	private boolean isPlay = false; //是否正在播放
	private boolean isRecord = false; //是否正在录音
	private int mRecordNum = 3; //播放高亮的波纹数
	private Timer mTimer; //定时器

	private float posX; //上次手指的x坐标
	private float posY; //上次手指的y坐标

	private RecordMode mRecordMode;//按钮类型

	private int recordId;//按钮的id

	public RecordView(Context context)
	{
		this(context, null);
	}

	public RecordView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public RecordView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mPaint = new Paint();

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordView);
		mRoundSize = typedArray.getDimension(R.styleable.RecordView_roundSize, 0);
		mBackgroundColor = typedArray.getColor(R.styleable.RecordView_backgroundColor, getResources().getColor(R.color.lightblue));
		mStrokeWidth = typedArray.getDimension(R.styleable.RecordView_strokeWidth, DipPxConversion.dip2px(context, 2));
		mRecordMode = typedArray.getInt(R.styleable.RecordView_recordMode, 0) == 1 ? RecordMode.RECORD : RecordMode.BROADCAST;
		typedArray.recycle();

		mRecordingColor = Color.WHITE;
		mRecordedColor = getResources().getColor(R.color.lightblue);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		mPaint.setAntiAlias(true);

		mWidth = getWidth();
		mHeight = getHeight();
		//绘制背景
		drawBackground(canvas);

		switch (mRecordMode)
		{
		case BROADCAST:
		{
			//绘制波纹
			float xCenter = mWidth / 2 - mHeight / 4;
			//绘制背景浅色波纹
			drawRecordArc(canvas, xCenter, mRecordedColor, 3);
			//
			drawRecordArc(canvas, xCenter, mRecordingColor, mRecordNum);
			break;
		}
		case RECORD:
		{
			drawRecord(canvas);
			break;
		}
		}
	}

	/**
	 * 绘制圆角背景
	 *
	 * @param canvas 画布
	 */
	private void drawBackground(Canvas canvas)
	{
		RectF rectF = new RectF(0, 0, mWidth, mHeight);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(mBackgroundColor);
		canvas.drawRoundRect(rectF, mRoundSize, mRoundSize, mPaint);
	}

	/**
	 * 绘制录音
	 *
	 * @param canvas
	 */
	private void drawRecord(Canvas canvas)
	{
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.record);

		Rect rectSrc = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		int margin = DipPxConversion.dip2px(getContext(), 16);
		int srcWidth = DipPxConversion.dip2px(getContext(), 24);
		Rect rectDst = new Rect((int) mWidth - margin - srcWidth, (int) (mHeight / 2 - srcWidth / 2), (int) mWidth - margin,
				(int) (mHeight / 2 + srcWidth / 2));
		canvas.drawBitmap(bitmap, rectSrc, rectDst, mPaint);
	}

	/**
	 * 绘制一条波纹
	 *
	 * @param canvas  画布
	 * @param xCenter 波纹的圆心x轴
	 * @param radius  波纹的半径
	 */
	private void drawArc(Canvas canvas, float xCenter, float radius, int color)
	{
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
	private void drawRecordArc(Canvas canvas, float xCenter, int color, int arcNum)
	{
		for (int i = 1; i <= arcNum; i++)
		{
			drawArc(canvas, xCenter, mHeight / 2 / 4 * i, color);
		}
	}

	private boolean shouldTouch(float x, float y)
	{
		if (x < getWidth() && x > 0)
			if (y < getHeight() && y > 0)
				return true;
		return false;
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
			case 1: //播放
			{
				if (mRecordNum < 3)
					mRecordNum++;
				else
					mRecordNum = 1;
				invalidate();
				break;
			}
			}
		}
	};

	private static final String TAG = "RecordView";

	private void setRecord(boolean isRecord)
	{
		this.isRecord = isRecord;
		if (recordViewListener != null)
			if (isRecord)
				recordViewListener.onRecordStart();
			else
				recordViewListener.onRecordStop();
	}

	public boolean isPlay()
	{
		return isPlay;
	}

	/**
	 * 设置播放或者停止
	 *
	 * @param play true :播放 false ：停止播放
	 */
	public void setPlay(boolean play)
	{
		isPlay = play;
		//监听点击事件
		if (mTimer != null)
		{
			//取消定时器
			mTimer.cancel();
			//恢复到波纹全部高亮显示
			mRecordNum = 3;
		}

		if (recordViewListener != null)
		{
			if (isPlay)
				recordViewListener.onBroadcastStart();
			else
				recordViewListener.onBroadcastStop();
		}
		//
		if (isPlay)
		{
			mTimer = new Timer();

			TimerTask task = new TimerTask()
			{
				@Override
				public void run()
				{
					//由于主线程安全，页面的更新需放到主线程中
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
			};
			mTimer.schedule(task, 300, 300);
		} else
		{
			//取消定时器
			mTimer.cancel();
			//恢复到波纹全部高亮显示
			mRecordNum = 3;
		}
		invalidate();
	}

	public void setPlay()
	{
		setPlay(!isPlay);
	}

	public RecordMode getRecordMode()
	{
		return mRecordMode;
	}

	public void setRecordMode(RecordMode mRecordMode)
	{
		this.mRecordMode = mRecordMode;
		invalidate();
	}

	public int getRecordId()
	{
		return recordId;
	}

	public void setRecordId(int recordId)
	{
		this.recordId = recordId;
	}

	private RecordViewListener recordViewListener;

	@Override
	public void onUpdate(int id, boolean isPlay)
	{
		if (getRecordId() == id)
			setPlay(isPlay);
	}

	/**
	 * 按钮的类型
	 */
	public enum RecordMode
	{
		//录音
		RECORD,

		//播音
		BROADCAST
	}

	public void setRecordViewListener(RecordViewListener recordViewListener)
	{
		this.recordViewListener = recordViewListener;
	}

	/**
	 * 点击事件
	 * 返回按钮的状态，是否处于播放状态
	 */
	public interface RecordViewListener
	{
		//开始播音
		void onBroadcastStart();

		//结束播音
		void onBroadcastStop();

		//开始录音
		void onRecordStart();

		//结束录音
		void onRecordStop();
	}
}
