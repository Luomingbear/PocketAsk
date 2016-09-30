package com.bear.pocketask.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * @author pan
 * dip与pix相互转换
 */
public class DipPxConversion
{
	private static String dipTag = "showTag";

	/**
	 * dip转化px
	 * 
	 * @param context
	 * @param dip
	 * @return
	 */
	public static int dipToPx(Context context, int dip)
	{
		if (null == context)
		{
			return 0;
		}
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	/**
	 * px转化dip
	 * 
	 * @param context
	 * @param px
	 * @return
	 */
	public static int pxToDip(Context context, int px)
	{
		if (null == context)
		{
			return 0;
		}
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
	}

	public static int dip2px(Context context, float dip)
	{
		int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
		return value;
	}

	public static int px2dip(Context context, float px)
	{
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return (int) dp;

	}

	/** 
	 * 将px值转换为sp值，保证文字大小不变 
	 *  
	 * @param pxValue 
	 *            （DisplayMetrics类中属性scaledDensity） 
	 * @return 
	 */
	public static int px2sp(Context context, float pxValue)
	{
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/** 
	 * 将sp值转换为px值，保证文字大小不变 
	 *  
	 * @param spValue 
	 *            （DisplayMetrics类中属性scaledDensity） 
	 * @return 
	 */
	public static int sp2px(Context context, float spValue)
	{
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
}
