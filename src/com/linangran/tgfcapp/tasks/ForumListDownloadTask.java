package com.linangran.tgfcapp.tasks;

import android.os.AsyncTask;
import com.linangran.tgfcapp.data.ForumListItemData;
import com.linangran.tgfcapp.data.HttpResult;
import com.linangran.tgfcapp.fragments.ForumListFragment;
import com.linangran.tgfcapp.network.NetworkUtils;

import java.util.List;

/**
 * Created by linangran on 3/1/15.
 */
public class ForumListDownloadTask extends AsyncTask<Integer, Integer, HttpResult<List<ForumListItemData>>>
{

	public ForumListFragment forumListFragment;

	public ForumListDownloadTask(ForumListFragment forumListFragment)
	{
		super();
		this.forumListFragment = forumListFragment;
	}

	@Override
	protected HttpResult<List<ForumListItemData>> doInBackground(Integer... params)
	{
		int fid = params[0];
		int page = params[1];
		return NetworkUtils.getForumList(fid, page);
	}

	@Override
	protected void onPostExecute(HttpResult<List<ForumListItemData>> result)
	{
		this.forumListFragment.finishLoadMoreData(result);
	}
}
