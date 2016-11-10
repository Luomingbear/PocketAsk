package com.bear.pocketask.widget.selectorbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.bear.pocketask.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 单选个多选的adapterView
 * Created by bear on 16/10/20.
 */

public class SelectorAdapterView extends AdapterView implements SelectorButton.SelectorChangeListener {
    private static final String TAG = "SelectorAdapterView";
    private int mCheckedId = -1; //选中的id
    private Adapter mAdapter; //数据适配器
    private AdapterDataSetObserver mDataSetObserver; //数据观察者
    private boolean isLayout; //是否正在布局
    private SelectorMode mSelectorMode; //选择模式

    /**
     * 初始化
     *
     * @param adapter               SelectorAdapter
     * @param selectorCheckListener SelectorCheckListener
     */
    public void init(Adapter adapter, SelectorCheckListener selectorCheckListener) {
        setAdapter(adapter);
        this.selectorCheckListener = selectorCheckListener;
    }

    @Override
    public void onChange(View view, boolean isChecked) {
        //设置选中的id
        mCheckedId = view.getId();
        //设置单选的状态
        if (mSelectorMode == SelectorMode.RadioButton && isChecked)
            setRadioButton();
        //返回选中的id列表
        if (selectorCheckListener != null)
            selectorCheckListener.onChecked(getCheckedIdList());
    }

    /**
     * 设置为单选效果
     */
    private void setRadioButton() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof SelectorButton && child.getId() != mCheckedId) {
                if (((SelectorButton) child).isChecked())
                    ((SelectorButton) child).setChecked(false);
            }
        }
    }

    /**
     * 得到选中的id列表
     *
     * @return id列表
     */
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

    private SelectorCheckListener selectorCheckListener;

    public void setSelectorCheckListener(SelectorCheckListener selectorCheckListener) {
        this.selectorCheckListener = selectorCheckListener;
    }

    /**
     * 选中的id的监听器
     */
    public interface SelectorCheckListener {
        //返回选中的按钮id
        void onChecked(List<Integer> checkedList);
    }

    /**
     * 数据更新观察者
     */
    private class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            requestLayout();
        }

    }

    public SelectorAdapterView(Context context) {
        this(context, null);
    }

    public SelectorAdapterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectorAdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectorAdapterView);
        mSelectorMode = SelectorMode.values()[typedArray.getInt(R.styleable.SelectorAdapterView_selectorMode, 0)];
        typedArray.recycle();
    }

    public enum SelectorMode {

        //多选
        CheckButton,
        //单选
        RadioButton

    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
            mDataSetObserver = null;
        }
        mAdapter = adapter;

        if (mAdapter != null && mDataSetObserver == null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (mAdapter == null)
            return;
        isLayout = true;

        if (mAdapter.getCount() == 0)
            removeAllViewsInLayout();
        else {
            layoutItems();
        }
        isLayout = false;

    }

    /**
     * 将选项显示在布局上
     */
    private void layoutItems() {
        if (mAdapter.getCount() == 0) {
            removeAllViews();
            return;
        }

        for (int i = 0; i < mAdapter.getCount(); i++) {
            newItem(i);
        }
    }

    /**
     * 创建选项
     */
    private void newItem(int index) {

        //获取适配器的view并且添加进去
        View item = mAdapter.getView(index, null, this);
        LayoutParams params = item.getLayoutParams();
        if (params == null)
            params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addViewInLayout(item, index, params);

        //是否需要重新计算宽高
        if (item.isLayoutRequested()) {
            int itemWidth = getWidth();
            item.measure(MeasureSpec.EXACTLY | itemWidth, MeasureSpec.UNSPECIFIED);
        }

        //设置显示的位置
        positionItem(item, index);

        //设置id
        if (item.getId() == View.NO_ID)
            item.setId(item.hashCode());

        //设置点击监听事件
        if (item instanceof SelectorButton) {
            ((SelectorButton) item).setSelectorChangeListener(this);
        }

    }

    /**
     * 设置选项的显示位置
     *
     * @param item  选项view
     * @param index 列表的位置
     */
    private void positionItem(View item, int index) {
        int width = getMeasuredWidth();
        int height = (int) getResources().getDimension(R.dimen.title_height);
        item.layout(0, height * index, width, height * (index + 1));
    }

    private float mTouchX, mTouchY; //手指按下的坐标

    @Override
    public void requestLayout() {
        if (!isLayout)
            super.requestLayout();
    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public View getSelectedView() {
        return null;
    }

    @Override
    public void setSelection(int position) {

    }

}
