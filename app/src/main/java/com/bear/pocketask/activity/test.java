package com.bear.pocketask.activity;

import android.app.Activity;
import android.os.Bundle;

import com.bear.pocketask.R;
import com.bear.pocketask.adapter.SelectorAdapter;
import com.bear.pocketask.info.SelectorInfo;
import com.bear.pocketask.utils.AdapterViewUtil;
import com.bear.pocketask.view.selectorbutton.SelectorAdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试控件
 * Created by bear on 16/10/16.
 */

public class test extends Activity implements SelectorAdapterView.SelectorCheckListener
{
	private static final String TAG = "test";
	private SelectorAdapterView selectorAdapterView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

		initView();
	}

	private void initView()
	{
		setAdapterView();
	}

	private void setAdapterView()
	{
		List<SelectorInfo> selectorInfoList = new ArrayList<SelectorInfo>();
		for (int i = 0; i < 10; i++)
		{
			SelectorInfo info = new SelectorInfo("FuCK" + i);
			selectorInfoList.add(info);
		}
		selectorAdapterView = (SelectorAdapterView) findViewById(R.id.selectorAdapterView);
		SelectorAdapter selectorAdapter = new SelectorAdapter(this, selectorInfoList);
		selectorAdapterView.init(selectorAdapter, this);
		AdapterViewUtil.FixHeight(selectorAdapterView);
	}

	@Override
	public void onChecked(List<Integer> checkedIdList)
	{
//		Log.i(TAG, "onChecked: List" + checkedIdList);
//		Toast.makeText(this, "您已选择" + checkedIdList.size(), Toast.LENGTH_SHORT).show();
	}
}
