package com.bear.pocketask.view.selectorbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bear.pocketask.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 单选或者多选的父控件
 * Created by bear on 16/10/16.
 */

public class SelectorGroup extends LinearLayout implements ViewGroup.OnHierarchyChangeListener, SelectorButton.SelectorChangeListener {
    private SelectorMode mSelectorMode;
    private int mCheckedId = -1;

    public SelectorGroup(Context context) {
        this(context, null);
    }

    public SelectorGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectorGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectorGroup);
        mSelectorMode = SelectorMode.values()[typedArray.getInt(R.styleable.SelectorGroup_selectorMode, 0)];
        typedArray.recycle();
        setOnHierarchyChangeListener(this);
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        if (parent == SelectorGroup.this && child instanceof SelectorButton) {
            int id = child.getId();
            // generates an id if it's missing
            if (id == View.NO_ID) {
                id = child.hashCode();
                child.setId(id);
            }
            ((SelectorButton) child).setSelectorChangeListener(this);
        }

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        if (parent == SelectorGroup.this && child instanceof SelectorButton) {
            ((SelectorButton) child).setSelectorChangeListener(null);
        }
    }

    private void setRadioButton() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof SelectorButton && child.getId() != mCheckedId) {
                if (((SelectorButton) child).isChecked())
                    ((SelectorButton) child).setChecked(false);
            }
        }
    }

    private List<Integer> getCheckedIdList() {
        List<Integer> checkedIdList = new ArrayList<Integer>();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof SelectorButton && ((SelectorButton) child).isChecked()) {
                checkedIdList.add(child.getId());
            }
        }
        return checkedIdList;
    }

    @Override
    public void onChange(View view, boolean isChecked) {
        mCheckedId = view.getId();
        if (mSelectorMode == SelectorMode.RadioButton) {
            setRadioButton();
        }
        if (selectorCheckedListener != null)
            selectorCheckedListener.onChecked(getCheckedIdList());
    }

    public enum SelectorMode {

        //多选
        CheckButton,
        //单选
        RadioButton

    }

    private SelectorCheckedListener selectorCheckedListener;

    public void setSelectorCheckedListener(SelectorCheckedListener selectorCheckedListener) {
        this.selectorCheckedListener = selectorCheckedListener;
    }

    /**
     * 选择的回调函数
     */
    public interface SelectorCheckedListener {
        void onChecked(List<Integer> checkedIdList);
    }
}
