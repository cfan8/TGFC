package com.linangran.tgfcapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.widget.ListView;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.activities.PostActivity;
import com.linangran.tgfcapp.adapters.ForumListAdapter;
import com.linangran.tgfcapp.data.ForumBasicData;
import com.linangran.tgfcapp.data.ForumListItemData;
import com.linangran.tgfcapp.data.HttpResult;
import com.linangran.tgfcapp.utils.ErrorHandlerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linangran on 2/1/15.
 */
public class ForumListFragment extends Fragment
{

	private ListView listView;
	private SwipeRefreshLayout swipeRefreshLayout;
	private ForumListAdapter forumListAdapter;
	private SwipeRefreshLayout.OnRefreshListener onRefreshListener;

	private ForumBasicData forumBasicData;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	public void reload(ForumBasicData forumBasicData)
	{
		this.forumBasicData = forumBasicData;
		this.forumListAdapter.abortTask();
		this.forumListAdapter = new ForumListAdapter(this.getActivity(), new ArrayList<ForumListItemData>(), this, this.forumBasicData.fid);
		this.listView.setAdapter(this.forumListAdapter);
		this.getActivity().setTitle(this.forumBasicData.name);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View fragmentView = inflater.inflate(R.layout.forum_list_fragment, container, false);
		this.listView = (ListView) fragmentView.findViewById(R.id.forum_list_fragment_list_view);
		List<ForumListItemData> list = new ArrayList<ForumListItemData>();
		Bundle bundle = getArguments();
		this.forumBasicData = (ForumBasicData) bundle.get("forumBasicData");
		this.forumListAdapter = new ForumListAdapter(this.getActivity(), list, this, this.forumBasicData.fid);
		this.getActivity().setTitle(this.forumBasicData.name);
		this.listView.setAdapter(this.forumListAdapter);
		this.swipeRefreshLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.forum_list_fragment_swipe_refresh);
		this.swipeRefreshLayout.setColorSchemeResources(R.color.googlered, R.color.googleblue, R.color.googleyellow, R.color.googlegreen);
		this.onRefreshListener = new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				ForumListFragment.this.swipeRefreshLayout.setRefreshing(true);
				forumListAdapter.refreshData();
			}
		};
		this.swipeRefreshLayout.setOnRefreshListener(this.onRefreshListener);
		return fragmentView;
	}

	public void finishLoadMoreData(HttpResult<List<ForumListItemData>> result)
	{
		if (checkError(result) == false)
		{
			this.forumListAdapter.updateDataCallback(result.result);
		}
	}

	public void finishRefresh(HttpResult<List<ForumListItemData>> result)
	{
		if (checkError(result) == false)
		{
			this.forumListAdapter.refreshDataCallback(result.result);
		}
		this.swipeRefreshLayout.setRefreshing(false);
	}

	public boolean checkError(HttpResult result)
	{
		if (result.hasError)
		{
			ErrorHandlerUtils.handleError(result, this.getActivity());
		}
		return result.hasError;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.menu_fragment_forum_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		this.forumListAdapter.abortTask();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menu_fragment_forum_list_add_post:
				Intent intent = new Intent(getActivity(), PostActivity.class);
				intent.putExtra("isReply", false);
				intent.putExtra("fid", forumBasicData.fid);
				startActivity(intent);
				return true;
			case R.id.menu_fragment_forum_list_refresh:
				onRefreshListener.onRefresh();
				return true;

		}
		return super.onOptionsItemSelected(item);
	}
}
