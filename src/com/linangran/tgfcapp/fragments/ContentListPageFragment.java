package com.linangran.tgfcapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.activities.ContentActivity;
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
	private SwipeRefreshLayout.OnRefreshListener onRefreshListener;

	private ContentListPageData pageData;
	public int tid;
	public int page;
	public int fid;
	public String title;


	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
	{
		Bundle bundle = getArguments();
		this.tid = bundle.getInt("tid");
		this.page = bundle.getInt("page");
		this.fid = bundle.getInt("fid");
		this.title = bundle.getString("title");
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
		this.listView.setOnScrollListener(new AbsListView.OnScrollListener()
		{
			int lastVisibleItem = 0;
			int actionBarHeight = getResources().getDimensionPixelSize(R.dimen.actionbarTotalSize);
			ContentActivity contentActivity = (ContentActivity) ContentListPageFragment.this.getActivity();
			int lastItemTop = actionBarHeight;
			int scrollThreshold = 5;

			@Override
			public void onScrollStateChanged(AbsListView absListView, int i)
			{

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				int currentTop = actionBarHeight;
				if (listView.getChildAt(0) != null)
				{
					currentTop = listView.getChildAt(0).getTop();
				}
				//Log.w("", "lastItem: " + lastVisibleItem + ", item: " + firstVisibleItem + "; lastTop: " + lastItemTop+ ", top: " + currentTop);
				if (lastVisibleItem < firstVisibleItem || (firstVisibleItem != 0 && lastVisibleItem == firstVisibleItem && lastItemTop - currentTop > scrollThreshold) || (firstVisibleItem == 0 && lastVisibleItem == 0 && lastItemTop - currentTop > scrollThreshold && currentTop <= 0))
				{
					contentActivity.hideActionBar();
				}
				else if (lastVisibleItem > firstVisibleItem || (firstVisibleItem != 0 && lastVisibleItem == firstVisibleItem && lastItemTop - currentTop < -scrollThreshold) || (firstVisibleItem == 0 && lastVisibleItem == 0 && lastItemTop - currentTop < -scrollThreshold))
				{
					contentActivity.showActionBar();
				}
				lastVisibleItem = firstVisibleItem;
				lastItemTop = currentTop;
			}
		});
		this.loadingIndicatorProgressBar = (ProgressBar) contentListFragmentView.findViewById(R.id.content_list_fragment_page_loading);
		this.loadFailTextView = (TextView) contentListFragmentView.findViewById(R.id.content_list_fragment_page_load_fail);
		this.swipeRefreshLayout = (SwipeRefreshLayout) contentListFragmentView.findViewById(R.id.content_list_fragment_page_swipe_refresh);
		this.loadFailTextView = (TextView) contentListFragmentView.findViewById(R.id.content_list_fragment_page_load_fail);
		this.swipeRefreshLayout.setColorSchemeResources(R.color.googlered, R.color.googleblue, R.color.googleyellow, R.color.googlegreen);
		this.onRefreshListener = new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				if (ContentListPageFragment.this.contentListAdapter.isTaskRunning() == false)
				{
					swipeRefreshLayout.setRefreshing(true);
					ContentListPageFragment.this.contentListAdapter.startDownloading(true);
				}
				else
				{
					swipeRefreshLayout.setRefreshing(false);
				}
			}
		};
		this.swipeRefreshLayout.setOnRefreshListener(this.onRefreshListener);
		int dpActionBarSize = (int) (getResources().getDimension(R.dimen.actionbarTotalSize) / getResources().getDisplayMetrics().density);
		this.swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12 + dpActionBarSize, getResources().getDisplayMetrics()));
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.menu_fragment_content_page, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menu_fragment_content_page_refresh:
				this.onRefreshListener.onRefresh();
				return true;
		} return super.onOptionsItemSelected(item);
	}
}
