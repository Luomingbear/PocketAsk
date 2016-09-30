package com.bear.pocketask.view.cardview;

import com.bear.pocketask.R;
import com.bear.pocketask.view.RecordView;
import com.facebook.rebound.Spring;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 卡片
 * Created by luoming on 9/30/2016.
 */
public class CardItemView extends FrameLayout
{

	private ImageView mHeadPic; //头像
	private RecordView mRecordView; //播放录音按钮
	private LinearLayout mReport; //举报按钮
	private TextView mUserName; //用户名
	private ImageView mDetailPic; //详细图片
	private View mEdit; //输入语音或者文字
	private View mSendButton; //发送按钮

	private Spring springX; //x坐标
	private Spring springY; //y坐标

	public CardItemView(Context context)
	{
		this(context, null);
	}

	public CardItemView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public CardItemView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		inflate(context, R.layout.card_item, this);

		mHeadPic = (ImageView) findViewById(R.id.card_item_head_pic);
		mRecordView = (RecordView) findViewById(R.id.card_item_record);
		mReport = (LinearLayout) findViewById(R.id.card_item_report);
		mUserName = (TextView) findViewById(R.id.card_item_user_name);
		mDetailPic = (ImageView) findViewById(R.id.card_item_detail_pic);
		mEdit = findViewById(R.id.card_item_edit);
		mSendButton = findViewById(R.id.card_item_send);
	}



}
