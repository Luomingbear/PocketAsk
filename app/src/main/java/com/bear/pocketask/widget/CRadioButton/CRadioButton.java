package com.bear.pocketask.widget.cradiobutton;

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

import com.bear.pocketask.R;

/**
 * 定位开关
 * Created by LiXiaoJuan  on 2016/10/24.
 */
public class CRadioButton extends View {
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

    float change;
    //小按钮每次刷新变化量
    float duration=20;
    float end;
    boolean isAnimation=false;

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

    private class CRadioState
    {
        private int count = 1;

        public void changeCRState()
        {
            count = (count + 1) % 2;
            this.setCRState(count);
        }

        public void setCRState(int state)
        {
            setApperance(state);
            switch (state)
            {
                case 0:
                    if (listener != null)
                        listener.openState();
                    break;
                case 1:
                    if (listener != null)
                        listener.closeState();
                    break;
            }
        }
    }

    public CRadioButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        text_store = new String[2];
        paint = new Paint();
        state = new CRadioState();

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
                text_store[1] = "非公开";
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
        ta.recycle();

        btn = new RectF(left_padding, top_padding, left_padding + btn_width, top_padding + btn_height);
        select = new RectF(left_padding + btn_width - switch_width, top_padding, left_padding + btn_width, top_padding + btn_height);

        change=btn_width-switch_width;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if(!isAnimation){
                if (btn.contains(event.getX() + left_padding, event.getY() + top_padding))
                {
                    state.changeCRState();
                }
            }
        }
        return true;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        //super.draw(canvas);

        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        paint.setColor(getResources().getColor(R.color.lightblue));
        canvas.drawRoundRect(btn, btn_radius, btn_radius, paint);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(select, btn_radius, btn_radius, paint);

        if(isAnimation){
            slideAnimation();
        }
        else {
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize(text_size);
            Rect bound = new Rect();
            paint.getTextBounds(text_show, 0, text_show.length(), bound);//字符串形成的长方形
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();//字体测量
            int baseline = (btn_height - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top + top_padding;
            canvas.drawText(text_show, text_x - bound.width() / 2, baseline, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width=btn_width;
        int height=btn_height;

        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getMode(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width=widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width=btn_width;
                break;
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                height=btn_height;
                break;
            case MeasureSpec.EXACTLY:
                height=heightSize;
        }

        setMeasuredDimension(width,height);
    }

    private void setApperance(int state)
    {
        end=state*change;
        text_show = text_store[state];
        text_x = left_padding + (state + 1) % 2 * switch_width + (btn_width - switch_width) / 2;
        //Log.e("view","show+x");
        //Log.e("change",Float.toString(change));

        duration*=-1;

        isAnimation=true;
        slideAnimation();
    }

    private void slideAnimation(){
        if(((end==0)&&(select.left<=end))||((end==(btn_width-switch_width))&&(select.left>=end))){
            isAnimation=false;
            select.left=end;
            select.right=select.left+switch_width;
            //Log.e("view",Boolean.toString(isAnimation));
        }
        else {
            select.left += duration*Math.sin((select.left/change)*Math.PI*0.9+0.1);
            select.right = select.left + switch_width;
            //Log.e("left",Float.toString(select.left));
            //Log.e("duration",Double.toString(duration*Math.sin((double)(select.left/change)*Math.PI)));
        }
        invalidate();
        //Log.e("view","刷新");
    }
}