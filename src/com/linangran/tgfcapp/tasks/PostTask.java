package com.linangran.tgfcapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.linangran.tgfcapp.data.HttpResult;
import com.linangran.tgfcapp.fragments.PostFragment;
import com.linangran.tgfcapp.utils.NetworkUtils;

/**
 * Created by linangran on 27/1/15.
 */
public class PostTask extends AsyncTask<Void, Integer, HttpResult<Boolean>>
{
	boolean isReply;
	boolean hasQuote;
	boolean isEdit;
	Integer fid;
	Integer tid;
	Integer quotePid;
	Integer editPid;
	String title;
	String content;

	PostFragment postFragment;

	public PostTask(PostFragment postFragment)
	{
		this.postFragment = postFragment;
	}

	@Override
	protected HttpResult<Boolean> doInBackground(Void... voids)
	{
		return NetworkUtils.post(isReply, hasQuote, isEdit, fid, tid, quotePid, editPid, title, content);
	}

	public void setPostData(boolean isReply, boolean hasQuote, boolean isEdit, Integer fid, Integer tid, Integer quotePid, Integer editPid, String title, String content)
	{
		this.isReply = isReply;
		this.hasQuote = hasQuote;
		this.isEdit = isEdit;
		this.fid = fid;
		this.tid = tid;
		this.quotePid = quotePid;
		this.editPid = editPid;
		this.title = title;
		this.content = content;
	}

	@Override
	protected void onPostExecute(HttpResult<Boolean> booleanHttpResult)
	{
		super.onPostExecute(booleanHttpResult);
		postFragment.finishPost(booleanHttpResult);
	}
}
