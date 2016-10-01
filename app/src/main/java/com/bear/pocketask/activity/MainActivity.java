package com.bear.pocketask.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bear.pocketask.R;
import com.bear.pocketask.info.CardItemInfo;
import com.bear.pocketask.view.cardview.SwipeFlingAdapterView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ArrayList<CardItemInfo> mViewList;
    private ArrayAdapter<CardItemInfo> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initImageViewLoader();

        initView();
    }

    /**
     * 初始网络图片加载器
     */
    public void initImageViewLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options).build();

        ImageLoader.getInstance().init(configuration);
    }

    private void initView() {
        mViewList = new ArrayList<CardItemInfo>();
        for (int i = 0; i < 10; i++) {
            CardItemInfo cardItemInfo = new CardItemInfo();
            cardItemInfo.setDetailPic("http://images.yeyou.com/2016/news/2016/09/29/x0929xx03s.jpg");
            cardItemInfo.setUserName("name" + i);
            mViewList.add(cardItemInfo);
        }

        SwipeFlingAdapterView flingAdapterView = (SwipeFlingAdapterView) findViewById(R.id.flingView);

//        CardAdapter cardAdapter = new CardAdapter(this,mViewList);
//        flingAdapterView.setAdapter(cardAdapter);

        arrayAdapter = new ArrayAdapter<CardItemInfo>(this,R.layout.card_item,R.id.card_item_user_name,mViewList);
        flingAdapterView.setAdapter(arrayAdapter);
        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Toast.makeText(getApplication(), "remove", Toast.LENGTH_SHORT).show();
                mViewList.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {

            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(getApplication(), "right", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                Toast.makeText(getApplication(), "empty", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onScroll(float v) {

            }
        });
    }

}
