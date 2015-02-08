package com.linangran.tgfcapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.*;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.activities.ContentActivity;
import com.linangran.tgfcapp.adapters.ContentListAdapter;
import com.linangran.tgfcapp.data.ContentListPageData;
import com.linangran.tgfcapp.data.HttpResult;
import com.linangran.tgfcapp.utils.ErrorHandlerUtils;
import com.linangran.tgfcapp.views.CustomScrollView;
import com.linangran.tgfcapp.views.ListLinearLayout;
import com.linangran.tgfcapp.views.OnScrollChangedListener;

/**
 * Created by linangran on 3/1/15.
 */
public class ContentListPageFragment extends Fragment
{
	private ListLinearLayout listLinearLayout;
	private CustomScrollView customScrollView;
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
		this.listLinearLayout = (ListLinearLayout) contentListFragmentView.findViewById(R.id.content_list_fragment_page_list_view);
		this.customScrollView = (CustomScrollView) contentListFragmentView.findViewById(R.id.content_list_fragment_page_scroll_view);
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
		this.customScrollView.setOnScrollChangedListener(new OnScrollChangedListener()
		{

			ContentActivity contentActivity = (ContentActivity) ContentListPageFragment.this.getActivity();
			int actionBarHeight = getResources().getDimensionPixelSize(R.dimen.actionbarTotalSize);

			@Override
			public void onScrollChanged(ScrollView scrollView, int l, int t, int oldl, int oldt)
			{
				if (t - oldt > 3 && t >= actionBarHeight)
				{
					contentActivity.hideActionBar();
				}
				else if (t < actionBarHeight || oldt - t > 3)
				{
					contentActivity.showActionBar();
				}
			}
		});
		this.listLinearLayout.setAdapter(this.contentListAdapter);

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
					if (ContentListPageFragment.this.contentListAdapter.getCount() != 0)
					{
						swipeRefreshLayout.setRefreshing(true);
					}
					else
					{
						showLoadingView();
					}
					ContentListPageFragment.this.contentListAdapter.startDownloading(true);
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
		this.loadFailTextView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showLoadingView();
				ContentListPageFragment.this.contentListAdapter.startDownloading(false);
			}
		});
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
		}
		return super.onOptionsItemSelected(item);
	}
}
