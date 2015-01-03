package com.linangran.tgfcapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.adapters.ForumListAdapter;
import com.linangran.tgfcapp.data.ForumListItemData;
import com.linangran.tgfcapp.data.HttpResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linangran on 2/1/15.
 */
public class ForumListFragment extends Fragment {

	private ListView listView;
	private SwipeRefreshLayout swipeRefreshLayout;
	private ForumListAdapter forumListAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.forum_list_fragment, container, false);
		this.listView = (ListView) fragmentView.findViewById(R.id.forum_list_fragment_list_view);
		List<ForumListItemData> list = new ArrayList<ForumListItemData>();
		this.forumListAdapter = new ForumListAdapter(this.getActivity(), list, this, 10);
		this.listView.setAdapter(this.forumListAdapter);
		this.swipeRefreshLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.forum_list_fragment_swipe_refresh);
		this.swipeRefreshLayout.setColorSchemeResources(R.color.googlered, R.color.googleblue, R.color.googleyellow, R.color.googlegreen);
		this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				ForumListFragment.this.swipeRefreshLayout.setRefreshing(true);
				forumListAdapter.refreshData();
			}
		});
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
			String text = "网络错误";
			if (result.errorInfo != null)
			{
				text += ": " + result.errorInfo;
			}
			Toast toast = Toast.makeText(this.getActivity(), text, Toast.LENGTH_SHORT);
			toast.show();
		}
		return result.hasError;
	}
}
