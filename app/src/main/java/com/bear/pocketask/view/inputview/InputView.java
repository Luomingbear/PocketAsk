package com.bear.pocketask.view.inputview;

import com.bear.pocketask.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 弹出的输入框
 * Created by ming.luo on 10/27/2016.
 */

public class InputView extends LinearLayout
{
	public InputView(Context context)
	{
		this(context, null);
	}

	public InputView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public InputView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView();
	}

	private void initView()
	{
		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);

		//输入框
		addView(newEditTextView());

		//发送按钮
		addView(newSendButton());
	}

	/**
	 * 生成发送按钮
	 * @return
	 */
	private LinearLayout newSendButton()
	{
		LinearLayout sendLayout = new LinearLayout(getContext());
		LayoutParams p = new LayoutParams((int) (getResources().getDimension(R.dimen.title_height)), (int) (getResources().getDimension(R.dimen.title_height)));
		sendLayout.setLayoutParams(p);
		sendLayout.setGravity(Gravity.CENTER);
		sendLayout.setBackgroundColor(getResources().getColor(R.color.deepblue));

		int iconWidth = (int) getResources().getDimension(R.dimen.icon_large);
		sendLayout.addView(newImageView(iconWidth, iconWidth, R.drawable.ok));
		return sendLayout;
	}

	/**
	 * 生成输入框
	 */
	private EditText newEditTextView()
	{
		LinearLayout editLayout = new LinearLayout(getContext());
		LayoutParams p = new LayoutParams((int) (getResources().getDimension(R.dimen.title_height)), (int) (getResources().getDimension(R.dimen.title_height)));
		editLayout.setLayoutParams(p);
		editLayout.setBackgroundColor(getResources().getColor(R.color.white));

		EditText editText = new EditText(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1);
		editText.setLayoutParams(params);
		return editText;
	}

	/**
	 * 生成图片
	 */
	private ImageView newImageView(int width, int height, int drawableId)
	{
		ImageView imageView = new ImageView(getContext());
		LayoutParams params = new LayoutParams(width, height);
		imageView.setLayoutParams(params);
		imageView.setImageResource(drawableId);
		return imageView;
	}
}
