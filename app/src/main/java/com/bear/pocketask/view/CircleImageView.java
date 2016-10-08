package com.bear.pocketask.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 圆形ImageView
 * Created by luoming on 9/30/2016.
 */
public class CircleImageView extends ImageView {
    int length, dstX, dstY; //矩形的边长，左上角的x，y坐标
    private static final String TAG = "CircleImageView";

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 绘制圆形imageview
     * <p>
     * 原理：绘制两层，第一层绘制bitmap，第二层绘制圆，然后设置叠加属性，使bitmap上圆外的被切除
     *
     * @param canvas canvas 不要继承父类的onDraw，否则绘制出来的imageview会有两层
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        //获取draable对象
        Drawable drawable = getDrawable();
        if (drawable == null)
            return;

		/* 绘制底层的bitmap */

        //判断宽度与高度并把小的设置为最大box的边长
        length = getWidth() > getHeight() ? getHeight() : getWidth();

        //设置显示的左上角坐标
        dstX = (getWidth() - length) / 2;
        dstY = (getHeight() - length) / 2;

        //转为圆形bitmap
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap roundBitmap = getCroppedRoundBitmap(bitmap);
        //绘制
        canvas.drawBitmap(roundBitmap, dstX, dstY, paint);

    }

    public Bitmap getCroppedRoundBitmap(Bitmap bmp) {
        Bitmap sbmp;
        int srcW = bmp.getWidth(), srcH = bmp.getHeight(), boxX, boxY, boxW;

        //选择图片的最小边作为最大box的边长
        boxW = srcW > srcH ? srcH : srcW;
        //计算最大box的在坐标
        boxX = (srcW - boxW) / 2;
        boxY = (srcH - boxW) / 2;

        //裁剪获取图片中心的正方形区域
        sbmp = Bitmap.createBitmap(bmp, boxX, boxY, boxW, boxW);
        //ARGB_8888具有透明属性
        Bitmap output = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        //
        final Paint paint = new Paint();
        final Rect rectSrc = new Rect(0, 0, boxW, boxW);
        final Rect rectDst = new Rect(0, 0, length, length);

        //设置画笔属性
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true); //滤波器打开，能减少马赛克
        paint.setDither(true);

        canvas.drawCircle(getWidth() / 2 - dstX, getHeight() / 2 - dstY, length / 2, paint);
        //设置叠加方式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rectSrc, rectDst, paint);

        return output;
    }
}
