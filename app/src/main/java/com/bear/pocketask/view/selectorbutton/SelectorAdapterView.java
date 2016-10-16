package com.bear.pocketask.view.selectorbutton;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.bear.pocketask.R;

import java.util.List;

/**
 * Created by bear on 16/10/20.
 */

public class SelectorAdapterView extends AdapterView implements SelectorButton.SelectorChangeListener {
    private List<String> mList; //选项数据集
    private Adapter mAdapter; //数据适配器
    private AdapterDataSetObserver mDataSetObserver; //数据观察者
    private boolean isLayout; //是否正在布局

    @Override
    public void onChange(View view, boolean isChecked) {
        //// TODO: 16/10/20 增加单选的控制 
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
        layoutItems();

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
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addViewInLayout(item, index, params);

        //是否需要重新计算宽高
        if (item.isLayoutRequested()) {
            int itemWidth = getWidth();
            item.measure(MeasureSpec.EXACTLY | itemWidth, MeasureSpec.UNSPECIFIED);
        }

        //设置显示的位置
        positionItem(item, index);
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
