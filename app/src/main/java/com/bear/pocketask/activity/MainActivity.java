package com.bear.pocketask.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bear.pocketask.R;
import com.bear.pocketask.adapter.CardAdapter;
import com.bear.pocketask.info.CardItemInfo;
import com.bear.pocketask.view.cardview.CardSlideAdapterView;
import com.bear.pocketask.view.record.RecordObservable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class MainActivity extends Activity implements CardAdapter.CardItemClickListener, CardSlideAdapterView.OnCardSlidingListener {
    private static final String TAG = "MainActivity";
    private ArrayList<CardItemInfo> mCardInfoList;
    private CardAdapter cardAdapter;

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
        for (int i = 0; i < 10; i++) {
            CardItemInfo cardItemInfo = new CardItemInfo();
            cardItemInfo.setUserName("海绵宝宝" + i);
            cardItemInfo.setQuestionId(i);
            cardItemInfo.setQuestions("请问长都大厦附近有好吃的冰淇淋吗？？+" + i);
            cardItemInfo.setHeadPic("http://1124.cc/up_files/2012-05-11/1301c63e0a.jpg");
            cardItemInfo.setDetailPic("http://img1.3lian.com/2015/a2/150/d/24.jpg");

            mCardInfoList.add(cardItemInfo);
        }

        CardSlideAdapterView flingAdapterView = (CardSlideAdapterView) findViewById(R.id.flingView);

        cardAdapter = new CardAdapter(this, mCardInfoList);
        cardAdapter.setCardItemClickListener(this);
        flingAdapterView.init(this, cardAdapter);

    }

    private boolean isPlay = false;

    @Override
    public void onClickedObject(CardAdapter.CardItemClickMode clickMode) {
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
                break;
            }
            case RECORD_BUTTON:
                //			Toast.makeText(this, "点击了录音", Toast.LENGTH_SHORT).show();
                isPlay = !isPlay;
                RecordObservable.getInstance().notifyObservers(mCardInfoList.get(0).getQuestionId(), isPlay);
        }
    }

    @Override
    public void onRemove() {
        Toast.makeText(this, "移除", Toast.LENGTH_SHORT).show();
        if (mCardInfoList.size() > 0) {
            mCardInfoList.remove(0);
            cardAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClicked() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, test.class);
        startActivity(intent);
        Toast.makeText(this, "点击了卡片", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onExitLeft() {


    }

    @Override
    public void onExitRight() {


    }
}
