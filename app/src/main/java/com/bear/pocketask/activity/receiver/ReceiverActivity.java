package com.bear.pocketask.activity.receiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.bear.pocketask.R;
import com.bear.pocketask.activity.test;
import com.bear.pocketask.adapter.CardAdapter;
import com.bear.pocketask.info.CardItemInfo;
import com.bear.pocketask.tools.observable.EventObservable;
import com.bear.pocketask.view.cardview.CardSlideAdapterView;
import com.bear.pocketask.view.dialog.InputDialog;
import com.bear.pocketask.view.inputview.ITextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class ReceiverActivity extends Activity implements CardAdapter.CardItemClickListener, CardSlideAdapterView.OnCardSlidingListener {
    private static final String TAG = "ReceiverActivity";
    private ArrayList<CardItemInfo> mCardInfoList; //数据集
    private CardAdapter cardAdapter; //卡片适配器
    private CardSlideAdapterView cardSlideAdapterView; //卡片自动生成
    private CharSequence input; //输入框的值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_activity);

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
        for (int i = 0; i < 5; i++) {
            CardItemInfo cardItemInfo = new CardItemInfo();
            cardItemInfo.setUserName("海绵宝宝" + i);
            cardItemInfo.setQuestionId(i);
            cardItemInfo.setQuestions("请问长都大厦附近有好吃的冰淇淋吗？？+" + i);
            cardItemInfo.setHeadPic("http://1124.cc/up_files/2012-05-11/1301c63e0a.jpg");
            cardItemInfo.setDetailPic("http://img1.3lian.com/2015/a2/150/d/24.jpg");

            mCardInfoList.add(cardItemInfo);
        }

        cardSlideAdapterView = (CardSlideAdapterView) findViewById(R.id.cardSlideView);

        cardAdapter = new CardAdapter(this, mCardInfoList);
        cardAdapter.setCardItemClickListener(this);
        cardSlideAdapterView.init(this, cardAdapter);

    }

    private boolean isPlay = false;

    @Override
    public void onClickedObject(int questionId, final CardAdapter.CardItemClickMode clickMode) {
        final int id = questionId;
        switch (clickMode) {
            case DETAIL_PIC: {
                //                Toast.makeText(this, "点击了说明图", Toast.LENGTH_SHORT).show();

                break;
            }
            case HEAD_PIC: {
                //                Toast.makeText(this, "点击了头像", Toast.LENGTH_SHORT).show();

                break;
            }
            case Report_BUTTON: {
                //                Toast.makeText(this, "点击了举报", Toast.LENGTH_SHORT).show();

                break;
            }
            case SEND_BUTTON: {
                //                Toast.makeText(this, "点击了发送", Toast.LENGTH_SHORT).show();
                break;
            }
            case INPUT_BUTTON:

                showInputPanel();
                break;

        }
    }

    /**
     * 显示输入面板
     */
    private void showInputPanel() {
        final InputDialog inputDialog = new InputDialog(this);
        inputDialog.setShowText(mCardInfoList.get(0).getInputText());
        inputDialog.init(new InputDialog.OnInputChangeListener() {

            @Override
            public void onTextChange(CharSequence s, int start, int before, int count) {
                input = s;
                mCardInfoList.get(0).setInputText(input.toString());
            }

            @Override
            public void onSendClick() {
                mCardInfoList.get(0).setInputText(input.toString());
                EventObservable.getInstance().notifyObservers(mCardInfoList.get(0).getQuestionId(), input.toString());

                inputDialog.dismiss();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
        });
    }

    @Override
    public void onRemove() {
        //        Toast.makeText(this, "移除", Toast.LENGTH_SHORT).show();
        if (mCardInfoList.size() > 0) {
            EventObservable.getInstance().deleteObserver((ITextView) cardSlideAdapterView.getSelectedView().findViewById(R.id.card_item_input));
            mCardInfoList.remove(0);
            cardAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClicked() {
        Intent intent = new Intent();
        intent.setClass(ReceiverActivity.this, test.class);
        startActivity(intent);
        //        Toast.makeText(this, "点击了卡片", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onExitLeft() {

    }

    @Override
    public void onExitRight() {

    }
}
