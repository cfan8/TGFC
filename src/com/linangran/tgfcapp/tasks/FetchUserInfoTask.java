package com.linangran.tgfcapp.tasks;

import android.os.AsyncTask;
import com.linangran.tgfcapp.activities.LoginActivity;
import com.linangran.tgfcapp.data.HttpResult;
import com.linangran.tgfcapp.utils.NetworkUtils;

/**
 * Created by linangran on 25/1/15.
 */
public class FetchUserInfoTask extends AsyncTask<Void, Integer, HttpResult<String>>
{
	private LoginActivity loginActivity;

	public FetchUserInfoTask(LoginActivity loginActivity)
	{
		this.loginActivity = loginActivity;
	}

	@Override
	protected HttpResult<String> doInBackground(Void... voids)
	{
		return NetworkUtils.fetchUsername();
	}

	@Override
	protected void onPostExecute(HttpResult<String> stringHttpResult)
	{
		super.onPostExecute(stringHttpResult);
		loginActivity.loadingUsernameFinish(stringHttpResult);
	}
}
