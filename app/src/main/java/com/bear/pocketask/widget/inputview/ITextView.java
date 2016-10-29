package com.bear.pocketask.widget.inputview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bear.pocketask.tools.observable.EventObserver;

/**
 * 增加观察者通知的文本框
 * Created by ming.luo on 10/28/2016.
 */

public class ITextView extends TextView implements EventObserver {
    private int questionId = -1;

    public ITextView(Context context) {
        super(context);
    }

    public ITextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ITextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    @Override
    public void onNotify(Object sender, int eventId, Object... args) {
        if (eventId == getQuestionId()) {
            this.setText(args[0].toString());
        }
    }
}
