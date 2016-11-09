package com.bear.pocketask.activity.create;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bear.pocketask.R;
import com.bear.pocketask.activity.base.BaseActivity;
import com.bear.pocketask.adapter.CreateSelectorAdapter;
import com.bear.pocketask.adapter.IViewPagerAdapter;
import com.bear.pocketask.info.CardItemInfo;
import com.bear.pocketask.info.SelectorInfo;
import com.bear.pocketask.utils.AdapterViewUtil;
import com.bear.pocketask.widget.inputview.InputDialog;
import com.bear.pocketask.widget.record.RecordView;
import com.bear.pocketask.widget.titleview.TitleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 创建问题
 * Created by bear on 16/10/31.
 */

public class CreateQuestionActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CreateQuestionActivity";
    private ViewPager viewPagerTop; //
    private TextView mTextQuestion; //文本的问题标题
    private RecordView mRecordView; //播放语音按钮
    private static int maxQuestionTextNum = 50; //文本的最大字数

    private ViewPager viewPagerBottom;
    private List<SelectorInfo> mSelectorList; //选项数据
    private ListView mLv_question; //选项列表
    private CreateSelectorAdapter mSelectoradapter; //选项适配器
    private static int maxSelectorTextNum = 16; //选项文本的最大字数
    private int maxSelectorNum = 4; //最大的选项数量
    //调用系统相册-选择图片
    private static final int IMAGE = 1;
    private static final int CAMERA = 2;
    private ImageView mChoosedImageView; //选中的图片
    private View mCamera; //相机
    private View mImage; //相册

    private CardItemInfo mCardInfo; //卡片的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_question_layout);

        initView();
    }

    private void initView() {
        mCardInfo = new CardItemInfo();

        initTitleView();

        initViewPager();
    }

    private void initTitleView() {
        TitleView titleView = (TitleView) findViewById(R.id.title_view);
        titleView.setOnTitleViewListener(new TitleView.OnTitleViewListener() {
            @Override
            public void onLeftButton() {
                finish();
            }

            @Override
            public void onRightButton() {
                previewCard();
            }
        });
    }

    private void initViewPager() {
        initTopViewPager();

        initBottomViewPager();
    }



    /**
     * 顶部的滚动view
     */
    private void initTopViewPager() {
        List<View> viewList = new ArrayList<View>();

        //编辑文本
        LayoutInflater lf = getLayoutInflater().from(this);

        View textQuestion = lf.inflate(R.layout.create_text_question_view, null);
        viewList.add(textQuestion);
        mTextQuestion = (TextView) viewList.get(0).findViewById(R.id.create_tv_question);
        mTextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestionInputDialog();
            }
        });

        //语音
        View recordQuestion = lf.inflate(R.layout.create_record_question_view, null);
        viewList.add(recordQuestion);
        mRecordView = (RecordView) viewList.get(1).findViewById(R.id.create_record_view);
        mRecordView.setClickRf();

        IViewPagerAdapter viewPagerAdapter = new IViewPagerAdapter(viewList);
        viewPagerTop = (ViewPager) findViewById(R.id.create_view_pager_top);
        viewPagerTop.setAdapter(viewPagerAdapter);

        //设置圆点的显示
        viewPagerTop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showViewPagerPoints(Gravity.TOP, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 显示问题标题的输入框
     */
    private void showQuestionInputDialog() {
        InputDialog inputDialog = new InputDialog(this);
        inputDialog.setShowText(mTextQuestion.getText().toString());
        inputDialog.init(new InputDialog.OnInputChangeListener() {
            @Override
            public void onTextChange(CharSequence s, int start, int before, int count) {
                if (start + count > maxQuestionTextNum) {
                    s = s.subSequence(0, maxQuestionTextNum);
                    Toast.makeText(getBaseContext(), getString(R.string.create_question_max_hint), Toast.LENGTH_SHORT).show();
                }

                mTextQuestion.setText(s.toString());
            }

            @Override
            public void onSendClick() {

            }
        });
    }

    /**
     * 底部的滚动view
     */
    private void initBottomViewPager() {
        List<View> viewList = new ArrayList<View>();

        //选项
        LinearLayout lv_layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lv_layout.setLayoutParams(params);
        lv_layout.setPadding(0, (int) getResources().getDimension(R.dimen.margin_normal), 0, 0);
        lv_layout.setGravity(Gravity.CENTER_VERTICAL);
        mLv_question = new ListView(this);
        mLv_question.setLayoutParams(params);
        mLv_question.setDivider(null);
        mLv_question.setFocusable(false);
        initSelector(mLv_question);

        //addView
        lv_layout.addView(mLv_question);
        viewList.add(lv_layout);
        //图片
        LayoutInflater lf = getLayoutInflater().from(this);
        View imageQuestion = lf.inflate(R.layout.create_picture_question_view, null);
        viewList.add(imageQuestion);

        IViewPagerAdapter viewPagerAdapter = new IViewPagerAdapter(viewList);
        viewPagerBottom = (ViewPager) findViewById(R.id.create_view_pager_bottom);
        viewPagerBottom.setAdapter(viewPagerAdapter);

        //设置圆点的显示
        viewPagerBottom.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //当前在第几页
                showViewPagerPoints(Gravity.BOTTOM, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        /**
         * 相机
         */

        //相机
        mCamera = viewList.get(1).findViewById(R.id.create_question_camera);
        mCamera.setOnClickListener(this);
        //相册
        mImage = viewList.get(1).findViewById(R.id.create_question_image);
        mImage.setOnClickListener(this);
        //显示图片
        mChoosedImageView = (ImageView) viewList.get(1).findViewById(R.id.create_iv_image);

    }

    /**
     * 设置圆点的显示
     *
     * @param gravity 圆点属于哪一个viewpager Gravity.TOP:上面 Gravity.BOTTOM:下面
     */
    private void showViewPagerPoints(int gravity, int position) {
        // TODO: 16/11/7  显示圆点的和变化
        RadioButton[] topPoints = new RadioButton[2];
        topPoints[0] = (RadioButton) findViewById(R.id.create_top_point1);
        topPoints[1] = (RadioButton) findViewById(R.id.create_top_point2);
        RadioButton[] bottomPoints = new RadioButton[2];
        bottomPoints[0] = (RadioButton) findViewById(R.id.create_bottom_point1);
        bottomPoints[1] = (RadioButton) findViewById(R.id.create_bottom_point2);

        switch (gravity) {
            case Gravity.TOP:
                topPoints[position].setChecked(true);
                break;
            case Gravity.BOTTOM:
                bottomPoints[position].setChecked(true);
                break;
        }
    }

    /**
     * 初始化选项设置一个显示的选项
     *
     * @param listView
     */
    private void initSelector(ListView listView) {
        mSelectorList = new ArrayList<SelectorInfo>();

        addSelector();

        inputSelectorText();

    }

    private void inputSelectorText() {
        mLv_question.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                showInputDialog(position);
            }
        });

    }

    private void showInputDialog(final int position) {
        final int i = position;
        InputDialog inputDialog = new InputDialog(this);
        inputDialog.setShowText(mSelectorList.get(position).getContent());
        inputDialog.init(new InputDialog.OnInputChangeListener() {
            @Override
            public void onTextChange(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "onTextChange: count++" + count);
                Log.i(TAG, "onTextChange: start++" + start);
                Log.i(TAG, "onTextChange: before++" + before);
                if (count + start > maxSelectorTextNum) {
                    s = s.subSequence(0, maxSelectorTextNum);
                    Toast.makeText(getBaseContext(), getString(R.string.create_selector_max_hint), Toast.LENGTH_SHORT).show();
                }
                mSelectorList.get(position).setContent(s.toString());
                invalidateSelectorListView();
            }

            @Override
            public void onSendClick() {

            }
        });
    }

    /**
     * 添加选项
     */
    private void addSelector() {
        if (mSelectorList.size() < maxSelectorNum) {
            mSelectorList.add(new SelectorInfo());
            invalidateSelectorListView();
        }
    }

    /**
     * 刷新选项数据
     */
    private void invalidateSelectorListView() {
        mSelectoradapter = new CreateSelectorAdapter(this, mSelectorList);
        mSelectoradapter.setOnAddSelectorListener(new CreateSelectorAdapter.OnAddSelectorListener() {
            @Override
            public void onAddClicked() {
                addSelector();
            }

            @Override
            public void onSubClicked(int position) {
                mSelectorList.remove(position);
                invalidateSelectorListView();
            }
        });
        mLv_question.setAdapter(mSelectoradapter);
        AdapterViewUtil.FixHeight(mLv_question);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_question_camera:
                takeImage();
                break;
            case R.id.create_question_image:
                chooseImage();
                break;
        }
    }

    private String imagePath = null;

    /**
     * 调用相机拍照
     */
    private void takeImage() {
        String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        imagePath = Environment.getExternalStorageDirectory() + "/PocketAsk/" + name;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imagePath)));
        startActivityForResult(intent, CAMERA);
    }

    /**
     * 调用系统相册
     */
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取相册图片
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);
            Toast.makeText(this, imagePath, Toast.LENGTH_SHORT).show();
            showImage(imagePath);
            c.close();
        }

        //获取相机拍摄的图片
        if (requestCode == CAMERA) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile",
                        "SD card is not avaiable/writeable right now.");
                return;
            }

            showImage(imagePath);
        }
    }

    /**
     * 显示图片
     *
     * @param path
     */
    private void showImage(String path) {
        if (TextUtils.isEmpty(imagePath))
            return;
        mChoosedImageView.setVisibility(View.VISIBLE);
        mCamera.setVisibility(View.GONE);
        mImage.setVisibility(View.GONE);
        ImageLoader.getInstance().displayImage("file://" + path, mChoosedImageView);
    }

    private void previewCard() {
        int top = viewPagerTop.getCurrentItem(), bottom = viewPagerBottom.getCurrentItem();
        //top
        switch (top) {
            case 0:
                mCardInfo.setQuestions(mTextQuestion.getText().toString());
                break;
            case 1:

                break;
        }

        //bottom
        switch (bottom) {
            case 0:
                for (int i = 0; i < mSelectorList.size(); i++) {
                    if (TextUtils.isEmpty(mSelectorList.get(i).getContent()))
                        mSelectorList.remove(i);
                }
                mCardInfo.setSelectorList(mSelectorList);
                break;
            case 1:
                mCardInfo.setDetailPic(imagePath);
                break;
        }

        if (top == 0 && bottom == 0) {
            mCardInfo.setCardMode(CardItemInfo.CardMode.TopTextBottomSelector);
        } else if (top == 0 && bottom == 1) {
            mCardInfo.setCardMode(CardItemInfo.CardMode.TopTextBottomImage);
        }
        if (top == 1 && bottom == 0) {
            mCardInfo.setCardMode(CardItemInfo.CardMode.TopAudioBottomSelector);
        }
        if (top == 1 && bottom == 1) {
            mCardInfo.setCardMode(CardItemInfo.CardMode.TopAudioBottomImage);
        }

        intentWithParcelable(PreviewActivity.class, "preview", mCardInfo);

    }
}
