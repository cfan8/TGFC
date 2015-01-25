package com.linangran.tgfcapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.adapters.ContentListAdapter;
import com.linangran.tgfcapp.data.ContentListPageData;
import com.linangran.tgfcapp.data.HttpResult;
import com.linangran.tgfcapp.utils.ErrorHandlerUtils;

/**
 * Created by linangran on 3/1/15.
 */
public class ContentListPageFragment extends Fragment
{
	private ListView listView;
	private SwipeRefreshLayout swipeRefreshLayout;
	private ContentListAdapter contentListAdapter;
	private ContentFragment viewPagerFragment;
	private RelativeLayout loadInfoLayout;
	private ProgressBar loadingIndicatorProgressBar;
	private TextView loadFailTextView;

	private ContentListPageData pageData;
	private int tid;
	private int page;


	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Bundle bundle = getArguments();
		this.tid = bundle.getInt("tid");
		this.page = bundle.getInt("page");
		View contentListFragmentView = inflater.inflate(R.layout.content_list_fragment_page, container, false);
		this.listView = (ListView) contentListFragmentView.findViewById(R.id.content_list_fragment_page_list_view);
		this.loadInfoLayout = (RelativeLayout) contentListFragmentView.findViewById(R.id.content_list_fragment_page_list_view_empty_view);
		if (bundle.containsKey("pagedata"))
		{
			ContentListPageData pagedata = (ContentListPageData) bundle.get("pagedata");
			this.contentListAdapter = new ContentListAdapter(this, tid, page, pagedata.dataList);
		}
		else
		{
			this.contentListAdapter = new ContentListAdapter(this, tid, page);
		}
		this.listView.setAdapter(this.contentListAdapter);
		this.loadingIndicatorProgressBar = (ProgressBar) contentListFragmentView.findViewById(R.id.content_list_fragment_page_loading);
		this.loadFailTextView = (TextView) contentListFragmentView.findViewById(R.id.content_list_fragment_page_load_fail);
		this.swipeRefreshLayout = (SwipeRefreshLayout) contentListFragmentView.findViewById(R.id.content_list_fragment_page_swipe_refresh);
		this.loadFailTextView = (TextView) contentListFragmentView.findViewById(R.id.content_list_fragment_page_load_fail);
		this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				if (ContentListPageFragment.this.contentListAdapter.isTaskRunning() == false)
				{
					swipeRefreshLayout.setRefreshing(true);
					ContentListPageFragment.this.contentListAdapter.startDownloading(true);
					swipeRefreshLayout.setColorSchemeResources(R.color.googlered, R.color.googleblue, R.color.googleyellow, R.color.googlegreen);
				}
				else
				{
					swipeRefreshLayout.setRefreshing(false);
				}
			}
		});
		this.viewPagerFragment = (ContentFragment) this.getParentFragment();
		if (bundle.containsKey("pagedata"))
		{
			showListViewContent();
		}
		else
		{
			showLoadingView();
		}
		return contentListFragmentView;
	}


	public void showLoadingView()
	{
		this.swipeRefreshLayout.setVisibility(View.GONE);
		this.loadInfoLayout.setVisibility(View.VISIBLE);
		this.loadingIndicatorProgressBar.setVisibility(View.VISIBLE);
		this.loadFailTextView.setVisibility(View.GONE);
	}

	public void showLoadFailView()
	{
		this.swipeRefreshLayout.setVisibility(View.GONE);
		this.loadInfoLayout.setVisibility(View.VISIBLE);
		this.loadingIndicatorProgressBar.setVisibility(View.GONE);
		this.loadFailTextView.setVisibility(View.VISIBLE);
	}

	public void showListViewContent()
	{
		this.swipeRefreshLayout.setVisibility(View.VISIBLE);
		this.loadInfoLayout.setVisibility(View.GONE);
	}

	public void updateContentList(HttpResult<ContentListPageData> pageDataResult)
	{
		if (pageDataResult.hasError)
		{
			ErrorHandlerUtils.handleError(pageDataResult, this.getActivity());
			if (this.contentListAdapter.isEmpty())
			{
				showLoadFailView();
			}
		}
		else
		{
			this.pageData = pageDataResult.result;
			this.viewPagerFragment.updatePagerInfo(this.pageData);
			this.contentListAdapter.updateContentDataList(this.pageData.dataList);
			showListViewContent();
		}
	}

	public void finishRefreshing()
	{
		this.swipeRefreshLayout.setRefreshing(false);
		showListViewContent();
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		this.contentListAdapter.abortTask();
	}
}
