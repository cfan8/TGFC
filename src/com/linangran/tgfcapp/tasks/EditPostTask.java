package com.linangran.tgfcapp.tasks;

import android.os.AsyncTask;
import com.linangran.tgfcapp.data.EditPostData;
import com.linangran.tgfcapp.data.HttpResult;
import com.linangran.tgfcapp.fragments.PostFragment;
import com.linangran.tgfcapp.utils.NetworkUtils;

/**
 * Created by linangran on 5/2/15.
 */
public class EditPostTask extends AsyncTask<Void, Integer, HttpResult<EditPostData>>
{
	int pid;
	int tid;
	PostFragment postFragment;

	public EditPostTask(int pid, int tid, PostFragment postFragment)
	{
		this.pid = pid;
		this.tid = tid;
		this.postFragment = postFragment;
	}

	@Override
	protected HttpResult<EditPostData> doInBackground(Void... params)
	{
		return NetworkUtils.fetchEditText(pid, tid);
	}

	@Override
	protected void onPostExecute(HttpResult<EditPostData> result)
	{
		postFragment.finishFetchEditText(result);
	}
}
