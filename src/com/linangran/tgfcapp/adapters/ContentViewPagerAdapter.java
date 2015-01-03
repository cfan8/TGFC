package com.linangran.tgfcapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.linangran.tgfcapp.fragments.ContentListPageFragment;

/**
 * Created by linangran on 3/1/15.
 */
public class ContentViewPagerAdapter extends FragmentStatePagerAdapter
{
	int totalPostCount = 1;

	public ContentViewPagerAdapter(FragmentManager fm)
	{
		super(fm);
	}


	@Override
	public Fragment getItem(int i)
	{
		Fragment fragment = new ContentListPageFragment();
		return fragment;
	}

	@Override
	public int getCount()
	{
		return 0;
	}
}
