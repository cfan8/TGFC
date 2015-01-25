package com.linangran.tgfcapp.tasks;

import android.os.AsyncTask;
import com.linangran.tgfcapp.data.ContentListPageData;
import com.linangran.tgfcapp.data.HttpResult;
import com.linangran.tgfcapp.fragments.ContentListPageFragment;
import com.linangran.tgfcapp.utils.NetworkUtils;

/**
 * Created by linangran on 5/1/15.
 */
public class ContentListDownloadTask extends AsyncTask<Integer, Integer, HttpResult<ContentListPageData>>
{
	private ContentListPageFragment contentListPageFragment;
	private boolean isRefreshing;

	public ContentListDownloadTask(ContentListPageFragment contentListPageFragment)
	{
		this.contentListPageFragment = contentListPageFragment;
	}

	public void setRefreshing(boolean isRefreshing)
	{
		this.isRefreshing = isRefreshing;
	}

	@Override
	protected HttpResult<ContentListPageData> doInBackground(Integer... inputs)
	{
		int tid = inputs[0], page = inputs[1];
		return NetworkUtils.getContentList(tid, page);
	}

	@Override
	protected void onPostExecute(HttpResult<ContentListPageData> result)
	{
		if (isRefreshing)
		{
			contentListPageFragment.finishRefreshing();
		}
		contentListPageFragment.updateContentList(result);
	}
}
