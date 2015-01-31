package com.linangran.tgfcapp.tasks;

import android.os.AsyncTask;
import com.linangran.tgfcapp.data.ContentListPageData;
import com.linangran.tgfcapp.data.HttpResult;
import com.linangran.tgfcapp.data.ImageDownloadInfo;
import com.linangran.tgfcapp.fragments.ContentListPageFragment;
import com.linangran.tgfcapp.utils.ImageDownloadManager;
import com.linangran.tgfcapp.utils.NetworkUtils;

import java.util.List;

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
		HttpResult<ContentListPageData> contentList = NetworkUtils.getContentList(tid, page);
		return contentList;
	}

	@Override
	protected void onPostExecute(HttpResult<ContentListPageData> result)
	{
		if (isRefreshing)
		{
			contentListPageFragment.finishRefreshing();
		}
		contentListPageFragment.updateContentList(result);
		if (result.hasError == false)
		{
			List<String> urlList = result.result.imgURLList;
			ImageDownloadManager manager = ImageDownloadManager.getInstance();
			for (int i = 0; i < urlList.size(); i++)
			{
				manager.addTask(new ImageDownloadTask(this.contentListPageFragment.getActivity(), new ImageDownloadInfo(urlList.get(i))));
				manager.startTask(urlList.get(i));
			}
		}
	}
}
