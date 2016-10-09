package com.bear.pocketask.activity;

import java.util.ArrayList;

import com.bear.pocketask.R;
import com.bear.pocketask.adapter.CardAdapter;
import com.bear.pocketask.info.CardItemInfo;
import com.bear.pocketask.view.cardview.SwipeFlingAdapterView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity
        implements SwipeFlingAdapterView.onFlingListener, SwipeFlingAdapterView.OnItemClickListener, CardAdapter.CardItemClickListener {

    private ArrayList<CardItemInfo> mCardInfoList;
    private CardAdapter cardAdapter;
    private boolean isInsertTouch = false; // 是否是点击的卡片里面的按钮

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
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options).build();

        ImageLoader.getInstance().init(configuration);
    }

    private void initView() {
        mCardInfoList = new ArrayList<CardItemInfo>();
        for (int i = 0; i < 50; i++) {
            CardItemInfo cardItemInfo = new CardItemInfo();
            cardItemInfo.setUserName("海绵宝宝" + i);
            cardItemInfo.setQuestions("请问长都大厦附近有好吃的冰淇淋吗？？+" + i);
            cardItemInfo.setHeadPic("http://1124.cc/up_files/2012-05-11/1301c63e0a.jpg");
            cardItemInfo.setDetailPic("http://img1.3lian.com/2015/a2/150/d/24.jpg");

            mCardInfoList.add(cardItemInfo);
        }

        SwipeFlingAdapterView flingAdapterView = (SwipeFlingAdapterView) findViewById(R.id.flingView);

        cardAdapter = new CardAdapter(this, mCardInfoList);
        cardAdapter.setCardItemClickListener(this);
        flingAdapterView.init(this, cardAdapter);

    }

    @Override
    public void removeFirstObjectInAdapter() {
        mCardInfoList.remove(0);
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLeftCardExit(Object dataObject) {

    }

    @Override
    public void onRightCardExit(Object dataObject) {

    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {

    }

    private static final String TAG = "MainActivity";

    @Override
    public void onScroll(float scrollProgressPercent) {
        //Log.d(TAG, "onScroll: scroll++"+scrollProgressPercent);
    }

    @Override
    public void onItemClicked(int itemPosition, Object dataObject) {
        if (!isInsertTouch) {
            Toast.makeText(this, "点击了卡片", Toast.LENGTH_SHORT).show();
        }
        isInsertTouch = false;
        Log.i(TAG, "onItemClicked: itemClick");

    }

    @Override
    public void onClickedObject(CardAdapter.CardItemClickMode clickMode, int questionId) {
        Log.i(TAG, "onClickedObject: insert");
        isInsertTouch = true;
        switch (clickMode) {
            case DETAIL_PIC: {
                Toast.makeText(this, "点击了说明图", Toast.LENGTH_SHORT).show();

                break;
            }
            case HEAD_PIC: {
                Toast.makeText(this, "点击了头像", Toast.LENGTH_SHORT).show();

                break;
            }
            case Report_BUTTON: {
                Toast.makeText(this, "点击了举报", Toast.LENGTH_SHORT).show();

                break;
            }
            case SEND_BUTTON: {
                Toast.makeText(this, "点击了发送", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
