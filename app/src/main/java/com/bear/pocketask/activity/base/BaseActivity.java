package com.bear.pocketask.activity.base;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by bear on 16/10/29.
 */

public class BaseActivity extends Activity {
    /**
     * 页面跳转
     *
     * @param cls
     */
    public void intentTo(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
    }

    public void intentWithFlag(Class<?> cls, int flag) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.setFlags(flag);
        startActivity(intent);
    }
}
