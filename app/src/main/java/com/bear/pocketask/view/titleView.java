package com.bear.pocketask.view;

import com.bear.pocketask.R;
import com.bear.pocketask.utils.DipPxConversion;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 标题栏
 * Created by luoming on 10/9/2016.
 */
public class TitleView extends LinearLayout
{
	private TitleMode mTitleMode;

	private TextView mTitleTextView;
	private ImageView mLeftImageView;
	private ImageView mRightImageView;

	private String titleText; //标题文本
	private int titleColor; //标题颜色
	private float titleSize; //标题大小

	public TitleView(Context context)
	{
		this(context, null);
	}

	public TitleView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public TitleView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		setBackgroundColor(Color.WHITE);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
		mTitleMode = TitleMode.values()[typedArray.getInt(R.styleable.TitleView_titleMode, 0)];
		titleText = typedArray.getString(R.styleable.TitleView_titleText);
		titleColor = typedArray.getColor(R.styleable.TitleView_titleColor, getResources().getColor(R.color.grayblue));
		titleSize = typedArray.getDimension(R.styleable.TitleView_titleSize, getResources().getDimension(R.dimen.font_big));
		typedArray.recycle();
		initView();
	}

	/**
	 * 初始化界面
	 */
	private void initView()
	{
		switch (mTitleMode)
		{
		case PERSON_TITLE_EDIT:
		{
			//左边的图标
			addLeftButton();
			//居中的标题
			mTitleTextView = newTextView(titleText, titleColor, titleSize);
			mTitleTextView.setGravity(Gravity.CENTER);
			addView(mTitleTextView);
			//右边的图标
			addRightButton();
			break;
		}
		}
	}

	private void addLeftButton()
	{
		mLeftImageView = newImageView();
		mLeftImageView.setImageResource(R.drawable.user);
		addView(mLeftImageView);
		mLeftImageView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mTitleViewListener != null)
					mTitleViewListener.onLeftButton();
			}
		});
	}

	private void addRightButton()
	{
		mRightImageView = newImageView();
		mRightImageView.setImageResource(R.drawable.edit);
		addView(mRightImageView);
		mRightImageView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mTitleViewListener != null)
					mTitleViewListener.onRightButton();
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthMode == MeasureSpec.AT_MOST)
			widthSize = DipPxConversion.dip2px(getContext(), 200);
		if (heightMode == MeasureSpec.AT_MOST)
			heightSize = DipPxConversion.dip2px(getContext(), 48);

		setMeasuredDimension(widthSize, heightSize);
	}

	/**
	 * 新建文本view
	 * @param text
	 * @param color
	 * @param size
	 * @return
	 */
	private TextView newTextView(String text, int color, float size)
	{
		TextView textView = new TextView(getContext());
		textView.setText(text);
		textView.setTextColor(color);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
		textView.setLayoutParams(params);

		return textView;
	}

	/**
	 * 新建图片view
	 */
	private ImageView newImageView()
	{
		ImageView imageView = new ImageView(getContext());
		LayoutParams params = new LayoutParams(DipPxConversion.dip2px(getContext(), 32), DipPxConversion.dip2px(getContext(), 32));
		int margin = (int) getResources().getDimension(R.dimen.margin_normal);
		//设置外边距
		params.setMargins(margin, 0, margin, 0);
		imageView.setLayoutParams(params);

		//设置裁剪属性
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		return imageView;
	}

	/**
	 * 设置标题文字
	 * @param s
	 */
	public void setTitleText(String s)
	{
		if (mTitleTextView != null)
			mTitleTextView.setText(s);
	}

	/**
	 * 设置标题颜色
	 * @param color
	 */
	public void setTitleColor(int color)
	{
		if (mTitleTextView != null)
			mTitleTextView.setTextColor(color);
	}

	/**
	 * 设置标题大小
	 * @param size
	 */
	public void setTitleSize(float size)
	{
		if (mTitleTextView != null)
			mTitleTextView.setTextSize(size);
	}

	/**
	 * 设置左边的图标
	 * @param drawableId
	 */
	public void setLeftImage(int drawableId)
	{
		if (mLeftImageView != null)
		{
			mLeftImageView.setImageResource(drawableId);
		}
	}

	/**
	 * 设置右边的图标
	 * @param drawableId
	 */
	public void setmRightImage(int drawableId)
	{
		if (mRightImageView != null)
		{
			mRightImageView.setImageResource(drawableId);
		}
	}

	public enum TitleMode
	{
		PERSON_TITLE_EDIT
	}

	private TitleViewListener mTitleViewListener;

	public void setTitleViewListener(TitleViewListener mTitleViewListener)
	{
		this.mTitleViewListener = mTitleViewListener;
	}

	public interface TitleViewListener
	{
		//点击了左边的按钮执行
		void onLeftButton();

		//点击了右边的按钮执行
		void onRightButton();
	}
}
