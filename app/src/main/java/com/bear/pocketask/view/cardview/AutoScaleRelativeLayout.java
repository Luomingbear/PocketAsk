package com.bear.pocketask.view.cardview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bear.pocketask.R;

/**
 * 自动缩放卡片比例
 * Created by luoming on 9/30/2016.
 */
public class AutoScaleRelativeLayout extends FrameLayout {
    private float mScaleRate; //缩放比例

    public AutoScaleRelativeLayout(Context context) {
        this(context, null);
    }

    public AutoScaleRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoScaleRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoScaleRelativeLayout);
        mScaleRate = typedArray.getFloat(R.styleable.AutoScaleRelativeLayout_scaleRate, 0.618f);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 调整高度
        float width = getMeasuredWidth();
        float height = width * mScaleRate;
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = (int) height;
        setLayoutParams(params);
    }
}
