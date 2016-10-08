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

public class MainActivity extends Activity implements SwipeFlingAdapterView.onFlingListener
{

	private ArrayList<CardItemInfo> mCardInfoList;
	private CardAdapter cardAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initImageViewLoader();

		initView();
	}

	/**
	 * 初始网络图片加载器
	 */
	public void initImageViewLoader()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options).build();

		ImageLoader.getInstance().init(configuration);
	}

	private void initView()
	{
		mCardInfoList = new ArrayList<CardItemInfo>();
		for (int i = 0; i < 10; i++)
		{
			CardItemInfo cardItemInfo = new CardItemInfo();
			cardItemInfo.setUserName("海绵宝宝" + i);
			cardItemInfo.setQuestions("请问长都大厦附近有好吃的冰淇淋吗？？+" + i);
			cardItemInfo.setHeadPic("http://1124.cc/up_files/2012-05-11/1301c63e0a.jpg");
			cardItemInfo.setDetailPic("http://img1.3lian.com/2015/a2/150/d/24.jpg");

			mCardInfoList.add(cardItemInfo);
		}

		SwipeFlingAdapterView flingAdapterView = (SwipeFlingAdapterView) findViewById(R.id.flingView);

		cardAdapter = new CardAdapter(this, mCardInfoList);
		flingAdapterView.init(this, cardAdapter);

	}

	@Override
	public void removeFirstObjectInAdapter()
	{
		mCardInfoList.remove(0);
		cardAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLeftCardExit(Object dataObject)
	{

	}

	@Override
	public void onRightCardExit(Object dataObject)
	{

	}

	@Override
	public void onAdapterAboutToEmpty(int itemsInAdapter)
	{

	}

	@Override
	public void onScroll(float scrollProgressPercent)
	{

	}
}
