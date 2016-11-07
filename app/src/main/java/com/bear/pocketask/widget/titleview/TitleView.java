package com.bear.pocketask.widget.titleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bear.pocketask.R;
import com.bear.pocketask.utils.DipPxConversion;

/**
 * 标题栏
 * Created by luoming on 10/9/2016.
 */
public class TitleView extends RelativeLayout {
    private static final String TAG = "TitleView";

    private TitleMode mTitleMode;
    private int mTitleHeight;//标题栏高度


    private TextView mTitleTextView;
    private LinearLayout mLeftLayout; //左边的按钮
    private LinearLayout mRightLayout; //右边的按钮
    private ImageView mLeftImageView;
    private ImageView mRightImageView;

    private String mTitleText; //标题文本
    private int mTitleColor; //标题颜色
    private float mTitleSize; //标题大小

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundColor(Color.WHITE);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
        mTitleMode = TitleMode.values()[typedArray.getInt(R.styleable.TitleView_titleMode, 0)];
        mTitleText = typedArray.getString(R.styleable.TitleView_titleText);
        mTitleColor = typedArray.getColor(R.styleable.TitleView_titleColor, getResources().getColor(R.color.lightblue));
        mTitleSize = typedArray.getDimension(R.styleable.TitleView_titleSize, getResources().getDimension(R.dimen.font_big));
        typedArray.recycle();

        mTitleHeight = (int) getResources().getDimension(R.dimen.title_height);
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        switch (mTitleMode) {
            case PERSON_TITLE_EDIT: {
                //左边的图标
                addLeftButton(R.drawable.person);
                //居中的标题
                addTitle();
                //右边的图标
                addRightButton(R.drawable.write);
                break;
            }
            case BACK_TITLE_GO: {
                addLeftButton(R.drawable.back);
                addTitle();
                addRightButton(R.drawable.goon);
                break;
            }
            case BACK_TITLE: {
                addLeftButton(R.drawable.back);
                addTitle();
                break;
            }
        }
    }

    private void addTitle() {
        mTitleTextView = newTextView(mTitleText, mTitleColor, mTitleSize);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setSingleLine();
        mTitleTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        addView(mTitleTextView);
    }

    private void addLeftButton(int drawableId) {
        mLeftLayout = new LinearLayout(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, mTitleHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = (int) getResources().getDimension(R.dimen.margin_normal);
        params.setMargins(margin, 0, margin, 0);
        mLeftLayout.setLayoutParams(params);
        mLeftLayout.setGravity(Gravity.CENTER);
        mLeftImageView = newImageView();
        mLeftImageView.setImageResource(drawableId);
        mLeftLayout.addView(mLeftImageView);
        addView(mLeftLayout);
        mLeftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitleViewListener != null)
                    mTitleViewListener.onLeftButton();
            }
        });
    }

    private void addRightButton(int drawableId) {
        mRightLayout = new LinearLayout(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, mTitleHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        int margin = (int) getResources().getDimension(R.dimen.margin_normal);
        params.setMargins(margin, 0, margin, 0);
        mRightLayout.setLayoutParams(params);
        mRightLayout.setGravity(Gravity.CENTER);
        mRightImageView = newImageView();
        mRightImageView.setImageResource(drawableId);
        mRightLayout.addView(mRightImageView);
        addView(mRightLayout);
        mRightLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitleViewListener != null)
                    mTitleViewListener.onRightButton();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST)
            widthSize = DipPxConversion.dip2px(getContext(), 200);
        if (heightMode == MeasureSpec.AT_MOST)
            heightSize = (int) getResources().getDimension(R.dimen.title_height);

        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * 新建文本view
     *
     * @param text
     * @param color
     * @param size
     * @return
     */
    private TextView newTextView(String text, int color, float size) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextColor(color);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.setMargins((int) getResources().getDimension(R.dimen.margin_large_more), 0, (int) getResources().getDimension(R.dimen.margin_large_more), 0);
        textView.setLayoutParams(params);

        return textView;
    }

    /**
     * 新建图片view
     */
    private ImageView newImageView() {
        ImageView imageView = new ImageView(getContext());
        LayoutParams params = new LayoutParams(DipPxConversion.dip2px(getContext(), 32), DipPxConversion.dip2px(getContext(), 32));
//        int margin = (int) getResources().getDimension(R.dimen.margin_normal);
//        //设置外边距
//        params.setMargins(margin, 0, margin, 0);
        imageView.setLayoutParams(params);

        //设置裁剪属性
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        if (mTitleTextView != null) {
//            int leftWidth = 0, rightWidth = 0;
//            if (mLeftLayout != null)
//                leftWidth = mLeftLayout.getWidth();
//            if (mRightLayout != null)
//                rightWidth = mRightLayout.getWidth();
//            if (leftWidth > rightWidth)
//                mTitleTextView.setPadding(0, 0, leftWidth - rightWidth, 0);
//            else
//                mTitleTextView.setPadding(rightWidth - leftWidth, 0, 0, 0);
//        }
    }

    /**
     * 设置标题文字
     *
     * @param s
     */
    public void setmTitleText(String s) {
        if (mTitleTextView != null)
            mTitleTextView.setText(s);
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    public void setmTitleColor(int color) {
        if (mTitleTextView != null)
            mTitleTextView.setTextColor(color);
    }

    /**
     * 设置标题大小
     *
     * @param size
     */
    public void setmTitleSize(float size) {
        if (mTitleTextView != null)
            mTitleTextView.setTextSize(size);
    }

    /**
     * 设置左边的图标
     *
     * @param drawableId
     */
    public void setLeftImage(int drawableId) {
        if (mLeftImageView != null) {
            mLeftImageView.setImageResource(drawableId);
        }
    }

    /**
     * 设置右边的图标
     *
     * @param drawableId
     */
    public void setmRightImage(int drawableId) {
        if (mRightImageView != null) {
            mRightImageView.setImageResource(drawableId);
        }
    }

    public enum TitleMode {
        //个人- 标题 - 编辑
        PERSON_TITLE_EDIT,

        //返回 - 标题 - 前进
        BACK_TITLE_GO,

        //返回 - 标题
        BACK_TITLE
    }

    private OnTitleViewListener mTitleViewListener;

    public void setOnTitleViewListener(OnTitleViewListener mTitleViewListener) {
        this.mTitleViewListener = mTitleViewListener;
    }

    public interface OnTitleViewListener {
        //点击了左边的按钮执行
        void onLeftButton();

        //点击了右边的按钮执行
        void onRightButton();
    }
}
