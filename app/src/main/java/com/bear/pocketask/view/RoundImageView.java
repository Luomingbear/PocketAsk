package com.bear.pocketask.view;

import com.bear.pocketask.R;
import com.bear.pocketask.utils.DipPxConversion;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 圆角图片,可以指定某个角为圆角，其他不变
 * Created by bear on 2016/10/8.
 */
public class RoundImageView extends ImageView
{
	/**
	 * 图片的类型，圆形、圆角、指定角圆角
	 */
	private int type;
	public static final int TYPE_CIRCLE = 0;
	public static final int TYPE_ROUND = 1;

	/**
	 * 指定某个角圆角
	 */
	private static final int TYPE_TOP_LEFT_AND_TOP_RIGHT = TYPE_ROUND + 1; //左上角和右上角是圆角
	private static final int TYPE_BOTTOM_LEFT_AND_BOTTOM_RIGHT = TYPE_TOP_LEFT_AND_TOP_RIGHT + 1; //左下角和右下角是圆角

	/**
	 * 圆角的大小
	 */
	private int mBorderRadius;
	/**
	 * 宽高比
	 */
	float scale = 0f;
	/**
	 * 绘图的Paint
	 */
	private Paint mBitmapPaint;
	/**
	 * 圆角的半径
	 */
	private int mRadius;
	/**
	 * 3x3 矩阵，主要用于缩小放大
	 */
	private Matrix mMatrix;
	/**
	 * 渲染图像，使用图像为绘制图形着色
	 */
	private BitmapShader mBitmapShader;
	/**
	 * view的宽度
	 */
	private int mWidth;
	private RectF mRoundRect;

	public RoundImageView(Context context, AttributeSet attrs)
	{

		super(context, attrs);
		mMatrix = new Matrix();
		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
		if (a != null)
		{
			mBorderRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_BorderRadius, DipPxConversion.dip2px(context, 9));// 默认为9dp
			type = a.getInt(R.styleable.RoundImageView_RoundType, TYPE_ROUND);
			scale = a.getFloat(R.styleable.RoundImageView_Scale, scale);
			a.recycle();
		}
	}

	public RoundImageView(Context context)
	{
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		if (scale > 0)
		{
			setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
			// Children are just made to fill our space.
			int childWidthSize = getMeasuredWidth();
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childWidthSize / scale), MeasureSpec.EXACTLY);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		/**
		 * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
		 */
		if (type == TYPE_CIRCLE)
		{
			mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
			mRadius = mWidth / 2;
			setMeasuredDimension(mWidth, mWidth);
		}

	}

	public void setScale(float scale)
	{
		if (scale > 0)
			this.scale = scale;
		requestLayout();
	}

	/**
	 * 初始化BitmapShader
	 */
	private void setUpShader()
	{
		Drawable drawable = getDrawable();
		if (drawable == null)
		{
			return;
		}

		Bitmap bmp = drawableToBitamp(drawable);
		// 将bmp作为着色器，就是在指定区域内绘制bmp
		mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		float scale = 1.0f;
		if (type == TYPE_CIRCLE)
		{
			// 拿到bitmap宽或高的小值
			int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
			scale = mWidth * 1.0f / bSize;

		} else
		{
			if (!(bmp.getWidth() == getWidth() && bmp.getHeight() == getHeight()))
			{
				// 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
				scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight() * 1.0f / bmp.getHeight());
			}
		}
		// shader的变换矩阵，我们这里主要用于放大或者缩小
		mMatrix.setScale(scale, scale);
		// 设置变换矩阵
		mBitmapShader.setLocalMatrix(mMatrix);
		// 设置shader
		mBitmapPaint.setShader(mBitmapShader);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if (getDrawable() == null)
		{
			return;
		}
		setUpShader();

		switch (type)
		{
		case TYPE_CIRCLE:
			canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
			break;
		case TYPE_BOTTOM_LEFT_AND_BOTTOM_RIGHT:
			drawSpecialCorner(canvas);
			break;
		case TYPE_TOP_LEFT_AND_TOP_RIGHT:
			drawSpecialCorner(canvas);
			break;
		case TYPE_ROUND:
			canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius, mBitmapPaint);
			break;
		}

	}

	/**
	 * 绘制特定的角
	 */
	private void drawSpecialCorner(Canvas canvas)
	{
		//原理：先绘制四个圆都是圆角的图片，然后把不需要圆角的地方补上
		canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius, mBitmapPaint);

		switch (type)
		{
		case TYPE_TOP_LEFT_AND_TOP_RIGHT:
			canvas.drawRect(0, mRoundRect.bottom - mBorderRadius, mBorderRadius, mRoundRect.bottom, mBitmapPaint);
			canvas.drawRect(mRoundRect.right - mBorderRadius, mRoundRect.bottom - mBorderRadius, mRoundRect.right, mRoundRect.bottom, mBitmapPaint);
			break;
		case TYPE_BOTTOM_LEFT_AND_BOTTOM_RIGHT:
			canvas.drawRect(mRoundRect.right - mBorderRadius, 0, mRoundRect.right, mBorderRadius, mBitmapPaint);
			canvas.drawRect(0, 0, mBorderRadius, mBorderRadius, mBitmapPaint);
			break;
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);

		// 圆角图片的范围
		if (type != TYPE_CIRCLE)
			mRoundRect = new RectF(0, 0, w, h);
	}

	/**
	 * drawable转bitmap
	 *
	 * @param drawable
	 * @return
	 */
	private Bitmap drawableToBitamp(Drawable drawable)
	{
		if (drawable instanceof BitmapDrawable)
		{
			BitmapDrawable bd = (BitmapDrawable) drawable;
			return bd.getBitmap();
		}
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}

	private static final String STATE_INSTANCE = "state_instance";
	private static final String STATE_TYPE = "state_type";
	private static final String STATE_BORDER_RADIUS = "state_border_radius";

	@Override
	protected Parcelable onSaveInstanceState()
	{
		Bundle bundle = new Bundle();
		bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
		bundle.putInt(STATE_TYPE, type);
		bundle.putInt(STATE_BORDER_RADIUS, mBorderRadius);
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state)
	{
		if (state instanceof Bundle)
		{
			Bundle bundle = (Bundle) state;
			super.onRestoreInstanceState(((Bundle) state).getParcelable(STATE_INSTANCE));
			this.type = bundle.getInt(STATE_TYPE);
			this.mBorderRadius = bundle.getInt(STATE_BORDER_RADIUS);
		} else
		{
			super.onRestoreInstanceState(state);
		}

	}

	public void setBorderRadius(int borderRadius)
	{
		int pxVal = DipPxConversion.dip2px(getContext(), borderRadius);
		if (this.mBorderRadius != pxVal)
		{
			this.mBorderRadius = pxVal;
			invalidate();
		}
	}

	public void setType(int type)
	{
		if (this.type != type)
		{
			this.type = type;
			if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE)
			{
				this.type = TYPE_CIRCLE;
			}
			requestLayout();
		}

	}
}
