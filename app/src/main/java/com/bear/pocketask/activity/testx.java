package com.bear.pocketask.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bear.pocketask.R;
import com.bear.pocketask.view.CRadioButton.CRadioButton;

/**
 * Created by Administrator on 2016/10/26.
 */

public class testx extends Activity{
    private String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crbtn_item);

        CRadioButton crb=(CRadioButton)findViewById(R.id.crb);
        crb.setCRListener(new CRadioButton.CRListener() {
            @Override
            public void openState() {
                Log.e(TAG,"open");
            }

            @Override
            public void closeState() {
                Log.e(TAG,"close");
            }
        });
        crb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.e(TAG,"Touch");
                return false;
            }
        });
    }
}
