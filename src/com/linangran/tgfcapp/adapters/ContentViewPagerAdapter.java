package com.linangran.tgfcapp.adapters;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.linangran.tgfcapp.data.ContentListItemData;
import com.linangran.tgfcapp.data.ContentListPageData;
import com.linangran.tgfcapp.fragments.ContentListPageFragment;
import com.linangran.tgfcapp.tasks.ForumListDownloadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by linangran on 3/1/15.
 */
public class ContentViewPagerAdapter extends FragmentStatePagerAdapter
{
	public int totalPostCount = 1;
	public int tid;
	private int totalPage = 1;
	public HashMap<Integer, ContentListPageData> cachedData = new HashMap<Integer, ContentListPageData>();


	public ContentViewPagerAdapter(FragmentManager fm, int tid)
	{
		super(fm);
		this.tid = tid;
	}

	public void setTotalPage(int totalPage)
	{
		if (totalPage != this.totalPage)
		{
			this.totalPage = totalPage;
			this.notifyDataSetChanged();
			//todo verify if we need to notify change
		}
	}

	@Override
	public Fragment getItem(int i)
	{
		Fragment fragment = new ContentListPageFragment();
		Bundle dataBundle = new Bundle();
		dataBundle.putInt("tid", tid);
		dataBundle.putInt("page", i + 1);
		if (cachedData.containsKey(i + 1))
		{
			dataBundle.putSerializable("pagedata", cachedData.get(i + 1));
		}
		fragment.setArguments(dataBundle);
		return fragment;
	}

	@Override
	public int getCount()
	{
		return totalPage;
	}

	public void updatePagerInfo(ContentListPageData pageData)
	{
		this.totalPostCount = pageData.totalReplyCount;
		this.setTotalPage(pageData.totalPageCount);
		this.cachedData.put(pageData.currentPage, pageData);
	}



}
