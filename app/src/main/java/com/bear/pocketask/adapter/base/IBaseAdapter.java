package com.bear.pocketask.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 基本适配器
 * Created by bear on 16/10/20.
 */

public abstract class IBaseAdapter extends BaseAdapter {
    private List<?> mList;
    private LayoutInflater mInflater;

    public IBaseAdapter(Context context, List<?> mList) {
        mInflater = LayoutInflater.from(context);
        this.mList = mList;
    }

    public List<?> getList() {
        return mList;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    @Override
    public long getItemId(int position) {
        return mList == null ? 0 : position;
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }
}
