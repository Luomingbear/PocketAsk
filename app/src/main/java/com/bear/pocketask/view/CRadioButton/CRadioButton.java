package com.bear.pocketask.view.CRadioButton;

import com.bear.pocketask.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 定位开关
 * Created by LiXiaoJuan  on 2016/10/24.
 */

public class CRadioButton extends View
{
	private Paint paint;

	private enum CRMode
	{
		//定位
		NEAR_ANYWHERE,
		//私密与否
		PRIVATE_PUBLIC
	}

	private CRMode crMode;

	//按钮位置
	private int left_padding;
	private int top_padding;

	//按钮大小及内部组件
	private int btn_width;
	private int btn_height;
	private int switch_width;
	private int btn_radius;
	private int text_size;
	// public Typeface text_font;
	private String text_store[];
	private String text_show;
	int text_x;

	private RectF select;
	private RectF btn;

	//==================*不同状态对应不同方法*===========================================
	public CRListener listener;
	CRadioState state;

	public interface CRListener
	{
		void openState();

		void closeState();
	}

	public void setCRListener(CRListener listener)
	{
		this.listener = listener;
	}

	public CRadioButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		text_store = new String[2];
		paint = new Paint();
		state = new CRadioState(this);

		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CRadioButton);

		crMode = CRMode.values()[ta.getInt(R.styleable.CRadioButton_crb_mode, 0)];
		switch (crMode)
		{
		case NEAR_ANYWHERE:
			text_store[0] = "任意";
			text_store[1] = "附近";
			break;
		case PRIVATE_PUBLIC:
			text_store[0] = "公开";
			text_store[0] = "非公开";
			break;
		}
		text_show = text_store[1];

		left_padding = ta.getDimensionPixelSize(R.styleable.CRadioButton_left_padding, 0);
		top_padding = ta.getDimensionPixelSize(R.styleable.CRadioButton_top_padding, 0);
		btn_width = ta.getDimensionPixelSize(R.styleable.CRadioButton_btn_width, getResources().getDimensionPixelSize(R.dimen.cr_btn_width));
		btn_height = ta.getDimensionPixelSize(R.styleable.CRadioButton_btn_height, getResources().getDimensionPixelSize(R.dimen.cr_btn_height));
		switch_width = ta.getDimensionPixelSize(R.styleable.CRadioButton_switch_width, getResources().getDimensionPixelSize(R.dimen.cr_switch_width));
		btn_radius = ta.getDimensionPixelSize(R.styleable.CRadioButton_btn_radius, getResources().getDimensionPixelSize(R.dimen.cr_btn_radius));

		text_size = ta.getDimensionPixelSize(R.styleable.CRadioButton_cr_text_size, getResources().getDimensionPixelSize(R.dimen.font_min));
		text_x = left_padding + (btn_width - switch_width) / 2;

		btn = new RectF(left_padding, top_padding, left_padding + btn_width, top_padding + btn_height);
		select = new RectF(left_padding + btn_width - switch_width, top_padding, left_padding + btn_width, top_padding + btn_height);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			if (btn.contains(event.getX() + left_padding, event.getY() + top_padding))
			{
				state.changeCRState();
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		//super.draw(canvas);

		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);

		paint.setColor(getResources().getColor(R.color.grayblue));
		canvas.drawRoundRect(btn, btn_radius, btn_radius, paint);
		paint.setColor(Color.WHITE);
		canvas.drawRoundRect(select, btn_radius, btn_radius, paint);

		paint.setColor(Color.WHITE);
		paint.setTextAlign(Paint.Align.LEFT);
		paint.setTextSize(text_size);
		Rect bound = new Rect();
		paint.getTextBounds(text_show, 0, text_show.length(), bound);//字符串形成的长方形
		Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();//字体测量
		int baseline = (btn_height - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top + top_padding;
		canvas.drawText(text_show, text_x - bound.width() / 2, baseline, paint);
	}

	public void setApperance(int state)
	{
		text_show = text_store[state];
		text_x = left_padding + (state + 1) % 2 * switch_width + (btn_width - switch_width) / 2;

		select.left = left_padding + state * (btn_width - switch_width);
		select.right = select.left + switch_width;
		this.invalidate();
	}
}
