package com.linangran.tgfcapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.*;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.adapters.ContentViewPagerAdapter;
import com.linangran.tgfcapp.data.ContentListPageData;

/**
 * Created by linangran on 3/1/15.
 */
public class ContentFragment extends Fragment
{
	private ViewPager viewPager;
	private ContentViewPagerAdapter contentViewPagerAdapter;
	private int tid;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Bundle bundle = getArguments();
		this.tid = bundle.getInt("tid");
		View contentFragment = inflater.inflate(R.layout.content_list_fragment, container, false);
		this.contentViewPagerAdapter = new ContentViewPagerAdapter(getChildFragmentManager(), tid);
		this.viewPager = (ViewPager) contentFragment.findViewById(R.id.content_list_fragment_view_pager);
		this.viewPager.setAdapter(this.contentViewPagerAdapter);
		return contentFragment;
	}

	public void updatePagerInfo(ContentListPageData pageData)
	{
		this.contentViewPagerAdapter.updatePagerInfo(pageData);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.menu_fragment_content_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
}
