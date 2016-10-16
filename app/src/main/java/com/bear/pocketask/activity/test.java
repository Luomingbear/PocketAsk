package com.bear.pocketask.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.bear.pocketask.R;
import com.bear.pocketask.adapter.SelectorAdapter;
import com.bear.pocketask.info.SelectorInfo;
import com.bear.pocketask.utils.AdapterViewUtil;
import com.bear.pocketask.view.selectorbutton.SelectorAdapterView;
import com.bear.pocketask.view.selectorbutton.SelectorGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bear on 16/10/16.
 */

public class test extends Activity implements SelectorGroup.SelectorCheckedListener {
    private static final String TAG = "test";
    private SelectorGroup selectorGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        initView();
    }

    private void initView() {
//        selectorGroup = (SelectorGroup) findViewById(R.id.selectorGroup);
//        selectorGroup.setSelectorCheckedListener(this);
        setAdaperView();
    }

    private void setAdaperView() {
        List<SelectorInfo> selectorInfoList = new ArrayList<SelectorInfo>();
        for (int i = 0; i < 10; i++) {
            SelectorInfo info = new SelectorInfo("FuCK" + i);
            selectorInfoList.add(info);
        }

        SelectorAdapterView selectorAdapterView = (SelectorAdapterView) findViewById(R.id.selectorAdapterView);
        SelectorAdapter selectorAdapter = new SelectorAdapter(this, selectorInfoList);
        selectorAdapterView.setAdapter(selectorAdapter);
        AdapterViewUtil.FixHeight(selectorAdapterView);
    }

    @Override
    public void onChecked(List<Integer> checkedIdList) {
        Log.i(TAG, "onChecked: List" + checkedIdList);
    }
}
