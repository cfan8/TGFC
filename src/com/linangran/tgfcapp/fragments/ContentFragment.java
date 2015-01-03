package com.linangran.tgfcapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.adapters.ContentViewPagerAdapter;

/**
 * Created by linangran on 3/1/15.
 */
public class ContentFragment extends Fragment
{
	private ViewPager viewPager;
	private ContentViewPagerAdapter contentViewPagerAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.contentViewPagerAdapter = new ContentViewPagerAdapter(getFragmentManager());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View contentFragment = inflater.inflate(R.layout.content_list_fragment, container, false);
		this.viewPager = (ViewPager) contentFragment.findViewById(R.id.content_list_fragment_view_pager);
		this.viewPager.setAdapter(this.contentViewPagerAdapter);
		return contentFragment;
	}
}
