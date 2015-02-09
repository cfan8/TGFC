package com.linangran.tgfcapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.*;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.activities.PostActivity;
import com.linangran.tgfcapp.adapters.ContentViewPagerAdapter;
import com.linangran.tgfcapp.data.ContentListPageData;

/**
 * Created by linangran on 3/1/15.
 */
public class ContentFragment extends Fragment
{
	private ViewPager viewPager;
	private ContentViewPagerAdapter contentViewPagerAdapter;
	public int tid;
	public int fid;
	public String title;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Bundle bundle = getArguments();
		boolean shouldExit = bundle.getBoolean("shouldExit" ,false);
		if (shouldExit == false)
		{
			this.tid = bundle.getInt("tid");
			this.fid = bundle.getInt("fid", Integer.MIN_VALUE);
			this.title = bundle.getString("title");
			View contentFragment = inflater.inflate(R.layout.content_list_fragment, container, false);
			this.contentViewPagerAdapter = new ContentViewPagerAdapter(getChildFragmentManager(), tid, fid, title);
			this.viewPager = (ViewPager) contentFragment.findViewById(R.id.content_list_fragment_view_pager);
			this.viewPager.setAdapter(this.contentViewPagerAdapter);
			return contentFragment;
		}
		else
		{
			return inflater.inflate(R.layout.content_list_fragment, container, false);
		}
	}

	public void updateThreadInfo(int fid, String title)
	{
		if (this.fid == Integer.MIN_VALUE)
		{
			this.fid = fid;
			this.title = title;
			this.getActivity().setTitle(this.title);
			this.contentViewPagerAdapter.updateThreadInfo(fid, title);
		}
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menu_fragment_content_list_reply:
				if (fid != Integer.MIN_VALUE)
				{
					Intent intent = new Intent(getActivity(), PostActivity.class);
					intent.putExtra("isReply", true);
					intent.putExtra("isEdit", false);
					intent.putExtra("mainTitle", title);
					intent.putExtra("fid", fid);
					intent.putExtra("tid", tid);
					intent.putExtra("hasQuote", false);
					startActivity(intent);
					return true;
				}
		}
		return super.onOptionsItemSelected(item);
	}
}
