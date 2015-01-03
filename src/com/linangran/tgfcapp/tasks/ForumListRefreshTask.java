package com.linangran.tgfcapp.tasks;

import com.linangran.tgfcapp.data.ForumListItemData;
import com.linangran.tgfcapp.data.HttpResult;
import com.linangran.tgfcapp.fragments.ForumListFragment;

import java.util.List;

/**
 * Created by linangran on 3/1/15.
 */
public class ForumListRefreshTask extends ForumListDownloadTask
{
	public ForumListRefreshTask(ForumListFragment forumListFragment)
	{
		super(forumListFragment);
	}

	@Override
	protected void onPostExecute(HttpResult<List<ForumListItemData>> result)
	{
		this.forumListFragment.finishRefresh(result);
	}
}
