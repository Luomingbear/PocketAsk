package com.bear.pocketask.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.bear.pocketask.R;
import com.bear.pocketask.view.selectorbutton.SelectorGroup;

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
        selectorGroup = (SelectorGroup) findViewById(R.id.selectorGroup);
        selectorGroup.setSelectorCheckedListener(this);
    }

    @Override
    public void onChecked(List<Integer> checkedIdList) {
        Log.i(TAG, "onChecked: List" + checkedIdList);
    }
}
