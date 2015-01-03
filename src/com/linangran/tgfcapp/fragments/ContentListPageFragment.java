package com.linangran.tgfcapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.linangran.tgfcapp.R;

/**
 * Created by linangran on 3/1/15.
 */
public class ContentListPageFragment extends Fragment
{
	private ListView listView;
	private SwipeRefreshLayout swipeRefreshLayout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View contentListFragmentView = inflater.inflate(R.layout.content_list_fragment, container, false);
		this.listView = (ListView) contentListFragmentView.findViewById(R.id.content_list_fragment_page_list_view);
		this.swipeRefreshLayout = (SwipeRefreshLayout) contentListFragmentView.findViewById(R.id.content_list_fragment_page_swipe_refresh);
		return contentListFragmentView;
	}
}
