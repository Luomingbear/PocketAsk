package com.bear.pocketask.activity.receiver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.bear.pocketask.R;
import com.bear.pocketask.activity.base.BaseActivity;
import com.bear.pocketask.activity.comment.CommentActivity;
import com.bear.pocketask.activity.edit.CreateQuestionActivity;
import com.bear.pocketask.activity.login.LoginActivity;
import com.bear.pocketask.activity.person.PersonActivity;
import com.bear.pocketask.adapter.CardAdapter;
import com.bear.pocketask.info.CardItemInfo;
import com.bear.pocketask.model.location.LocationManager;
import com.bear.pocketask.tools.observable.EventObservable;
import com.bear.pocketask.widget.cardview.CardSlideAdapterView;
import com.bear.pocketask.widget.inputview.ITextView;
import com.bear.pocketask.widget.inputview.InputDialog;
import com.bear.pocketask.widget.titleview.TitleView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class ReceiverActivity extends BaseActivity implements CardAdapter.CardItemClickListener, CardSlideAdapterView.OnCardSlidingListener {
    private static final String TAG = "ReceiverActivity";
    private ArrayList<CardItemInfo> mCardInfoList; //数据集
    private CardAdapter mCardAdapter; //卡片适配器
    private CardSlideAdapterView mCardSlideAdapterView; //卡片自动生成
    private CharSequence input; //输入框的值

    private boolean isLogin = false; //是否登录帐号
    private LocationManager mLocationManager; //定位管理器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkIsLogin();

        setContentView(R.layout.receiver_layout);

        initImageViewLoader();

        initView();


        startLocation();
    }

    /**
     * 是否已经登录
     */
    private void checkIsLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginLog", MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("isLogin", false);
        boolean isTourist = sharedPreferences.getBoolean("isTourist", false);
        if (!isLogin && !isTourist)
            intentWithFlag(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //清除之前的activity堆栈
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
        initCards();

        initTitleView();
    }

    private void initTitleView() {
        TitleView titleView = (TitleView) findViewById(R.id.title_view);
        titleView.setOnTitleViewListener(new TitleView.OnTitleViewListener() {
            @Override
            public void onLeftButton() {
                intentTo(PersonActivity.class);
            }

            @Override
            public void onRightButton() {
                intentTo(CreateQuestionActivity.class);

            }
        });
    }

    /**
     * 初始化卡片
     */
    private void initCards() {
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

        mCardSlideAdapterView = (CardSlideAdapterView) findViewById(R.id.cardSlideView);

        mCardAdapter = new CardAdapter(this, mCardInfoList);
        mCardAdapter.setCardItemClickListener(this);
        mCardSlideAdapterView.init(this, mCardAdapter);

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
                //点击了头像，如果没有登录帐号则跳转到登录界面
                if (!isLogin)
                    intentTo(LoginActivity.class);
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
                if (input != null) {
                    mCardInfoList.get(0).setInputText(input.toString());
                    EventObservable.getInstance().notifyObservers(mCardInfoList.get(0).getQuestionId(), input.toString());
                }

                inputDialog.dismiss();

                inputDialog.hideKeyboard();
            }
        });
    }

    @Override
    public void onRemove() {
        //        Toast.makeText(this, "移除", Toast.LENGTH_SHORT).show();
        if (mCardInfoList.size() > 0) {
            EventObservable.getInstance().deleteObserver((ITextView) mCardSlideAdapterView.getSelectedView().findViewById(R.id.card_item_input));
            mCardInfoList.remove(0);
            mCardAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClicked() {
        Intent intent = new Intent();
        intent.setClass(ReceiverActivity.this, CommentActivity.class);
        startActivity(intent);
        //        Toast.makeText(this, "点击了卡片", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onExitLeft() {
        //卡片左移
    }

    @Override
    public void onExitRight() {
        //卡片右移

    }

    /**
     * 定位
     */
    private void startLocation() {
        //初始化定位
        mLocationManager = new LocationManager(getApplicationContext(), new LocationManager.OnLocationListener() {
            @Override
            public void onLocationSucceed(AMapLocation amapLocation) {
                Toast.makeText(getApplication(),amapLocation.getAoiName(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationManager.destroyLocationClient();
    }
}
