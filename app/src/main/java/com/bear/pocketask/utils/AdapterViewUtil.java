package com.bear.pocketask.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * AdapterView高度问题使用这个修复
 * Created by bear on 16/10/20.
 */

public class AdapterViewUtil {
    public static void FixHeight(AdapterView adapterView) {
        Adapter adapter = adapterView.getAdapter();
        if (adapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, adapterView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = adapterView.getLayoutParams();
        params.height = totalHeight;
        if (adapterView instanceof ListView)
            params.height = totalHeight + (((ListView) adapterView).getDividerHeight() * adapter.getCount() - 1);
        adapterView.setLayoutParams(params);
    }
}
