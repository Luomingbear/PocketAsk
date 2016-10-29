package com.bear.pocketask.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

/**
 * 基本的弹窗:d=宽度和屏幕相同
 * Created by ming.luo on 10/28/2016.
 */

public class IBaseDialog extends Dialog
{

	public IBaseDialog(Context context)
	{
		this(context, 0);
	}

	public IBaseDialog(Context context, int themeResId)
	{
		super(context, themeResId);

		//取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		WindowManager manager = getWindow().getWindowManager();
		Display display = manager.getDefaultDisplay();
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = display.getWidth();
		getWindow().setAttributes(params);
	}
}
