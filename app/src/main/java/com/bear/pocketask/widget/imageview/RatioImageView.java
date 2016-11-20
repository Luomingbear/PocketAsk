package com.bear.pocketask.widget.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bear.pocketask.R;

/**
 * 可以自定义比例的图片显示view
 * Created by bear on 2016/11/20.
 */

public class RatioImageView extends ImageView {
    private float ratio;

    public RatioImageView(Context context) {
        this(context, null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
        ratio = typedArray.getFloat(R.styleable.RatioImageView_ratio, 0.68f);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 调整高度
        float width = getMeasuredWidth();
        float height = width * ratio;
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = (int) height;
        setLayoutParams(params);
    }
}
